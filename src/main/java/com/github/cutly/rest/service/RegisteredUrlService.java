package com.github.cutly.rest.service;

import com.github.cutly.rest.domain.RegisteredUrl;
import com.github.cutly.rest.model.Hash;
import com.github.cutly.rest.model.LongUrl;
import com.github.cutly.rest.model.UrlUsage;
import com.github.cutly.rest.repository.RegisteredUrlRepository;
import com.github.cutly.rest.repository.SequenceIdRepository;
import com.mongodb.DuplicateKeyException;
import org.apache.log4j.Logger;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.github.cutly.rest.constants.Constants.URLS_SEQUENCE_ID;

@Service
public class RegisteredUrlService {

    private static final Logger logger = Logger.getLogger(RegisteredUrlService.class);

    @Autowired
    private SequenceIdRepository sequenceIdRepository;

    @Autowired
    private RegisteredUrlRepository registeredUrlRepository;

    private final Hashids hashids;

    public RegisteredUrlService() {
        this.hashids = new Hashids();
    }

    /**
     * persists a long url with its short url hash and retries if the value is not unique
     * @param longUrl the url that will by represented by the short url
     * @return the hash
     */
    public Hash saveShortUrl(LongUrl longUrl) {
        RegisteredUrl registeredUrl = null;

        while (true) {
            try {
                registeredUrl = createRegisteredUrl(longUrl);
                registeredUrlRepository.save(registeredUrl);
                break;
            } catch (DuplicateKeyException e) {
                logger.warn("Tried url" + registeredUrl.toString() + " but it is not unique. Retrying...", e);
            }
        }

        return new Hash(registeredUrl.getHash());
    }

    /**
     * create a new short url for a long url
     * @param longUrl the long url that will be represented by the short url
     * @return registered url with hash, long url, and sequence id
     */
    private RegisteredUrl createRegisteredUrl(LongUrl longUrl) {

        RegisteredUrl registeredUrl = new RegisteredUrl();
        registeredUrl.setLongUrl(longUrl.getLongUrl());

        // increase the sequence number. We will use the sequence number to calculate the hash
        // to help ensure uniqueness
        registeredUrl.setSequenceId(sequenceIdRepository.getNextSequenceId(URLS_SEQUENCE_ID));

        // create obfuscation of sequence id in order to ensure urls cannot be easily guessed
        String hash = hashids.encode(registeredUrl.getSequenceId());
        registeredUrl.setHash(hash);

        return registeredUrl;
    }

    /**
     * get the long url to redirect the user to
     * @param hash the hash that represents the long url
     * @return the long url for redirect
     */
    public LongUrl getLongUrl(String hash) {
        RegisteredUrl url = getRegisteredUrl(hash);
        url.getAccessDates().add(new Date());
        registeredUrlRepository.save(url);
        return new LongUrl(url.getLongUrl());
    }

    /**
     * get the all-time usage statistics for the short url
     * @param hash the hash representing the long url
     * @return all-time usage count and access dates
     */
    public UrlUsage getUrlUsage(String hash) {
        RegisteredUrl url = getRegisteredUrl(hash);

        // format dates
        List<String> accessDates = new ArrayList<String>();
        for (Date accessDate : url.getAccessDates()) {
            // format date
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String date = dt.format(accessDate);
            accessDates.add(date);
        }
        return new UrlUsage(url.getAccessDates().size(), accessDates);
    }

    /**
     * get the number of times the url was used in the last x days
     * @param hash the hash representing the long url
     * @param days from x days
     * @return access dates and total usage count for last x days
     */
    public UrlUsage getUrlUsage(String hash, int days) {
        RegisteredUrl url = getRegisteredUrl(hash);
        List<String> accessDates = new ArrayList<String>();

        Date benchmark = getCurrentTimeMinusDays(days);
        for (Date accessDate : url.getAccessDates()) {
            if (accessDate.after(benchmark) || accessDate.equals(benchmark)) {
                // format date
                SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String date = dt.format(accessDate);
                accessDates.add(date);
            }
        }
        return new UrlUsage(accessDates.size(), accessDates);
    }

    /**
     * Looks up the long url from the database based on the hash
     * @param hash that represents the long url
     * @return the registered url
     */
    private RegisteredUrl getRegisteredUrl(String hash) {
        long[] ids = hashids.decode(hash);
        if(ids.length != 1) {
            logger.warn("Invalid Url was entered");
            throw new IllegalArgumentException("Short url is not valid");
        }
        RegisteredUrl url = registeredUrlRepository.findBySequenceId(ids[0]);
        if (url == null) {
            logger.warn("Invalid Url was entered");
            throw new IllegalArgumentException("Short url is not valid");
        }
        return url;
    }

    /**
     * get current time minus x days
     * @param days number of days to subtract from current time
     * @return the time at x days ago
     */
    private Date getCurrentTimeMinusDays(int days) {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(days);
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
