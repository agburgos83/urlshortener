package com.challenge.urlshortener.domain;

public class UrlAccessStats {
    private String shortUrl;
    private Long creationTimestamp;  // Timestamp de creación
    private Long lastAccessedTimestamp;  // Timestamp del último acceso

    public UrlAccessStats(String shortUrl, Long creationTimestamp, Long lastAccessedTimestamp) {
        this.shortUrl = shortUrl;
        this.creationTimestamp = creationTimestamp;
        this.lastAccessedTimestamp = lastAccessedTimestamp;
    }  

    public String getShortUrl() {
        return shortUrl;
    }

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }

    public Long getlastAccessedTimestamp() {
        return lastAccessedTimestamp;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public void setCreationTimestamp(Long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public void setLlastAccessedTimestamp(Long lastAccessedTimestamp) {
        this.lastAccessedTimestamp = lastAccessedTimestamp;
    }
}

