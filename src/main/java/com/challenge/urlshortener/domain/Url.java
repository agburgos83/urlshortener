package com.challenge.urlshortener.domain;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Url implements Serializable {

    @JsonProperty
    private String shortUrl;

    @JsonProperty
    private String longUrl;

    @JsonProperty
    private boolean enabled;

    @JsonCreator
    public Url() {}

    public Url(String longUrl, String shortUrl, boolean status) {
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
        this.enabled = status;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String originalUrl) {
        this.longUrl = originalUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean status) {
        this.enabled = status;
    }

    @Override
    public String toString() {
        return "URL [longURL=" + longUrl + ", shortURL=" + shortUrl + ", status=" + enabled +" ]";
    }
}
