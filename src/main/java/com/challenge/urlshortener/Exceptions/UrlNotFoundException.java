package com.challenge.urlshortener.Exceptions;

public class UrlNotFoundException extends RuntimeException {
 
    public UrlNotFoundException(String message) {
        super(message);
    }
    
}
