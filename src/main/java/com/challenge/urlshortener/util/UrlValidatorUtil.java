package com.challenge.urlshortener.util;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;
import com.challenge.urlshortener.Exceptions.InvalidUrlException;

@Component
public class UrlValidatorUtil {

    private static UrlValidator urlValidator;
        
    public UrlValidatorUtil() {
        UrlValidatorUtil.urlValidator = new UrlValidator(new String[]{"http", "https"});
    }
    
    public static void validate(String url) {
        
        String firstThreeChars = url.substring(0, 3).toLowerCase();

        if(firstThreeChars.equals("www")) {
            String adaptedUrl = "https://" + url.substring(4);
            url = adaptedUrl;
        }

        if(!urlValidator.isValid(url)) {
            throw new InvalidUrlException("Formato de url inválido.");
        }
    }

}
