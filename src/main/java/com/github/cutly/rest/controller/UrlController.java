package com.github.cutly.rest.controller;

import com.github.cutly.rest.model.Hash;
import com.github.cutly.rest.model.LongUrl;
import com.github.cutly.rest.model.UrlUsage;
import com.github.cutly.rest.service.RegisteredUrlService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping(value = "/api/v1/urls")
public class UrlController {

    private static final Logger logger = Logger.getLogger(UrlController.class);

    @Autowired
    private RegisteredUrlService registeredUrlService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Hash> createShortUrl(@Valid @RequestBody LongUrl longUrl) {
        Hash hash = registeredUrlService.saveShortUrl(longUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(hash);
    }

    @RequestMapping(method = RequestMethod.GET, value="/{hash}")
    public void redirect(@PathVariable(value = "hash") String hash, HttpServletResponse httpServletResponse) throws IOException {
        // capture start time
        LocalDateTime start = LocalDateTime.now();
        logger.info("Starting redirect");

        LongUrl longUrl = registeredUrlService.getLongUrl(hash);
        httpServletResponse.sendRedirect(longUrl.getLongUrl().toString());

        // log end time
        LocalDateTime end = LocalDateTime.now();
        logger.info("Total time taken for redirect is " +
                LocalDateTime.from(start).until(end, ChronoUnit.MILLIS) + "ms");
    }

    @RequestMapping(method = RequestMethod.GET, value="/{hash}/usage")
    public ResponseEntity<UrlUsage> getUrlUsage(@RequestParam(required = false, value = "days") Integer days,
                                                @PathVariable(value = "hash") String hash) {
        UrlUsage usage = null;
        if(days != null)
            usage = registeredUrlService.getUrlUsage(hash, days);
        else
            usage = registeredUrlService.getUrlUsage(hash);
        return ResponseEntity.status(HttpStatus.OK).body(usage);
    }
}


