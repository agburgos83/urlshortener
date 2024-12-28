package com.challenge.urlshortener.Exceptions;

public class UrlNotEnabledException extends RuntimeException {
 
    public UrlNotEnabledException(String message) {
        super(message);
    }
    
}
