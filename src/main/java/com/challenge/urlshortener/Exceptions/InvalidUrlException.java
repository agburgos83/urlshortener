package com.challenge.urlshortener.Exceptions;

public class InvalidUrlException extends RuntimeException {
    
    public InvalidUrlException(String message) {
        super(message);
    }
    
}
