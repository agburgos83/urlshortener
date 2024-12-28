package com.challenge.urlshortener.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.InvalidUrlException;
import com.challenge.urlshortener.DTOs.UrlRequest;
import com.challenge.urlshortener.Exceptions.UrlNotFoundException;
import com.challenge.urlshortener.domain.Url;
import com.challenge.urlshortener.services.UrlService;

@RestController
@RequestMapping
public class UrlController {

    String dominio = "http://localhost:8080/redirect/";

    @Autowired
    private UrlService urlService;

    // acortar url
    @PostMapping("/shorten")
    public ResponseEntity<String> createShortenedUrl(@RequestBody UrlRequest urlRequest)
            throws UnsupportedEncodingException {
        try {
            String shortUrl = urlService.shortenUrl(urlRequest.getLongUrl());
            String fullShortUrl = dominio + shortUrl;
            return ResponseEntity.status(HttpStatus.CREATED).body(fullShortUrl);
        } catch (InvalidUrlException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // resolver una URL corta y rdirigirla a la URL larga
    @GetMapping("/redirect/{shortUrl}")
    public ResponseEntity<String> redirecToLongUrl(@PathVariable String shortUrl) {
        try {
            String longUrl = urlService.resolveUrl(shortUrl);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(longUrl)).build();
        } catch (UrlNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // habilitar o deshabilitar URL
    @PutMapping("/enable/{shortUrl}")
    public ResponseEntity<String> enableOrDisableUrl(@PathVariable String shortUrl, @RequestParam boolean enable) {
        try {
            urlService.updateUrlStatus(shortUrl, enable);
            boolean status = enable ? true : false;
            if (status) {
                return ResponseEntity.ok("La URL fue habilitada");
            } else {
                return ResponseEntity.ok("La URL fue deshabilitada");
            }

        } catch (UrlNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/update/{shortUrl}")
    public ResponseEntity<String> updateLongUrl(@PathVariable String shortUrl, @RequestBody UrlRequest urlRequest) {

        try {
            urlService.updateLongUrl(shortUrl, urlRequest.getLongUrl());
            return ResponseEntity.ok("Destino de URL actualizado");
        } catch (UrlNotFoundException | InvalidUrlException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // @GetMapping("/allUrls")
    // public ResponseEntity<List<Url>> findAllUrls() {
    // List<Url> urlsList = urlService.findAll();
    // return ResponseEntity.ok(urlsList);
    // }

    @GetMapping("/find/{shortUrl}")
    public ResponseEntity<Url> findUrl(@PathVariable String shortUrl) {
        Url foundUrl = urlService.findUrl(shortUrl);
        return ResponseEntity.ok(foundUrl);
    }

    @GetMapping("/stats/{shortUrl}")
    public ResponseEntity<String> getAccessStats(@PathVariable String shortUrl) {
        try {
            String accessCount = urlService.getAccessStats(shortUrl);
            return ResponseEntity.ok("Cantidad de veces que se accedió a la url " + shortUrl + ": " + accessCount);
        } catch (UrlNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
