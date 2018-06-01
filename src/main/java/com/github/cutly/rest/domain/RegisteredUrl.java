package com.github.cutly.rest.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "urls")
@TypeAlias("urls")
public class RegisteredUrl {

    @Id
    private String id;

    @Indexed(unique = true)
    private Long sequenceId;

    @Indexed(unique = true)
    private String hash;

    private URL longUrl;

    private List<Date> accessDates;

    public RegisteredUrl() {
        accessDates = new ArrayList<Date>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Long sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public URL getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(URL longUrl) {
        this.longUrl = longUrl;
    }

    public List<Date> getAccessDates() {
        return accessDates;
    }

    public void setAccessDates(List<Date> accessDates) {
        this.accessDates = accessDates;
    }

    @Override
    public String toString() {
        return "RegisteredUrl{" +
                "id='" + id + '\'' +
                ", sequenceId=" + sequenceId +
                ", hash='" + hash + '\'' +
                ", longUrl='" + longUrl + '\'' +
                '}';
    }
}
