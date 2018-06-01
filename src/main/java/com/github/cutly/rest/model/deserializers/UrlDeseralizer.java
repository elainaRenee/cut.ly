package com.github.cutly.rest.model.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.cutly.rest.component.DataLoader;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class UrlDeseralizer extends JsonDeserializer<URL> {

    private static final Logger logger = Logger.getLogger(UrlDeseralizer.class);

    private Pattern urlPrefix = Pattern.compile("^(https?://).*");

    @Override
    public URL deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        ObjectCodec objectCodec = parser.getCodec();
        JsonNode node = objectCodec.readTree(parser);
        String stringUrl = node.asText();
        URL url = null;
        try {
            if (!urlPrefix.matcher(stringUrl).matches()) {
                url = new URL("http://" + stringUrl);
            } else {
                url = new URL(stringUrl);
            }
        } catch (MalformedURLException e) {
            logger.error("User entered invalid URL");
            throw new MalformedURLException("Url is invalid");
        }
        return url;
    }

}
