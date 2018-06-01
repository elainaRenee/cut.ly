package com.github.cutly.rest.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.cutly.rest.model.deserializers.UrlDeseralizer;

import javax.validation.constraints.NotNull;
import java.net.URL;

public class LongUrl {

    @JsonDeserialize(using = UrlDeseralizer.class)
    @NotNull
    private URL longUrl;

    public LongUrl() {

    }

    public LongUrl(URL longUrl) {
        this.longUrl = longUrl;
    }

    public URL getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(URL longUrl) {
        this.longUrl = longUrl;
    }

    @Override
    public String toString() {
        return "LongUrl{" +
                "longUrl=" + longUrl +
                '}';
    }
}
