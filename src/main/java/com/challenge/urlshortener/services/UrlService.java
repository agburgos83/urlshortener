package com.challenge.urlshortener.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.challenge.urlshortener.Exceptions.ExistentUrlException;
import com.challenge.urlshortener.Exceptions.UrlNotEnabledException;
import com.challenge.urlshortener.Exceptions.UrlNotFoundException;
import com.challenge.urlshortener.domain.Url;
import com.challenge.urlshortener.util.UrlValidatorUtil;

@Service
public class UrlService {

    private final RedisTemplate<String, Url> redisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;
    private final RedisTemplate<String, Integer> integerRedisTemplate;

    private final String redisKeyPrefix = "url:";
    private static final Logger log = LoggerFactory.getLogger(UrlService.class);

    public UrlService(RedisTemplate<String, Url> redisTemplate, RedisTemplate<String, String> stringRedisTemplate,
            RedisTemplate<String, Integer> integerRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.integerRedisTemplate = integerRedisTemplate;
    }

    // acortar url
    public String shortenUrl(String longUrl) {
        UrlValidatorUtil.validate(longUrl);

        if (checkLongUrlDoesNotExist(longUrl) != null) {
            throw new ExistentUrlException("La url ya existe.");
        }

        String shortUrl = generateShortUrl();

        Url url = new Url(longUrl, shortUrl, true);
        saveUrlToRedis(shortUrl, url);
        return shortUrl;

    }

    // redirigir de una url corta a su direccion real
    public String resolveUrl(String shortUrl) {

        String redisKey = "url:" + shortUrl;
        Url url = redisTemplate.opsForValue().get(redisKey);

        if (url == null) {
            throw new UrlNotFoundException("La url no existe.");
        }

        if (!url.isEnabled()) {
            throw new UrlNotEnabledException("La url no está habilitada.");
        }

        String statsKey = "stats:" + shortUrl;
        redisTemplate.opsForValue().increment(statsKey);

        return (url.getLongUrl());
    }

    // cuantas veces se accedió a esa url corta
    public String getAccessStats(String shortUrl) {
        String statsKey = "stats:" + shortUrl;

        Integer accessCount = integerRedisTemplate.opsForValue().get(statsKey);

        if (accessCount == null) {
            throw new UrlNotFoundException("No se encontraron estadísticas para la URL corta: " + shortUrl);
        }

        return accessCount.toString();
    }

    // habilitar/deshabilitar
    public void updateUrlStatus(String shortUrl, boolean isEnabled) {

        Url url = getUrlFromRedis(shortUrl);
        if (url == null) {
            throw new UrlNotFoundException("La url no se encontró.");
        }
        url.setEnabled(isEnabled);
        saveUrlToRedis(shortUrl, url);
    }

    // actualizar direccion destino
    public void updateLongUrl(String shortUrl, String newLongUrl) {
        UrlValidatorUtil.validate(newLongUrl);
        Url url = getUrlFromRedis(shortUrl);
        if (url == null) {
            throw new UrlNotFoundException("La url no se encontró.");
        }
        url.setLongUrl(newLongUrl);
        saveUrlToRedis(shortUrl, url);
    }

    // generar url corta
    private String generateShortUrl() {
        String shortUrl;
        do {
            shortUrl = java.util.UUID.randomUUID().toString().substring(0, 8);
        } while (redisTemplate.opsForValue().get(redisKeyPrefix + shortUrl) != null);
        return shortUrl;
    }

    // guardar Url en redis
    private void saveUrlToRedis(String shortUrl, Url url) {

        if (url == null) {
            log.error("El objeto Url es nulo. No se puede guardar en Redis.");
            return;
        }

        String key = redisKeyPrefix + shortUrl;
        redisTemplate.opsForValue().set(key, url);

        String longUrlKey = redisKeyPrefix + ":longUrl:" + url.getLongUrl().hashCode();
        stringRedisTemplate.opsForValue().set(longUrlKey, shortUrl);

    }

    // obtener url
    private Url getUrlFromRedis(String shortUrl) {
        String key = redisKeyPrefix + shortUrl;
        return (Url) redisTemplate.opsForValue().get(key);
    }

    // verificar que la url larga no exista
    private Url checkLongUrlDoesNotExist(String longUrl) {
        String longUrlKey = redisKeyPrefix + ":longUrl:" + longUrl.hashCode();
        String shortUrl = stringRedisTemplate.opsForValue().get(longUrlKey);

        if (shortUrl == null) {
            return null;
        } else {
            String urlKey = redisKeyPrefix + shortUrl;
            return (Url) redisTemplate.opsForValue().get(urlKey);
        }

    }

    // public List<Url> findAll() {
    // // Obtiene todas las claves que coincidan con el prefijo
    // Set<String> keys = redisTemplate.keys(redisKeyPrefix + "*");
    // if (keys == null || keys.isEmpty()) {
    // return Collections.emptyList();
    // }

    // // Recupera todas las URLs usando las claves
    // return redisTemplate.opsForValue().multiGet(keys).stream()
    // .filter(Objects::nonNull) // Filtra valores nulos
    // .map(obj -> (Url) obj)
    // .collect(Collectors.toList());
    // }

    // encontrar una url
    public Url findUrl(String shortUrl) {
        String redisKey = "url:" + shortUrl;
        Object rawData = redisTemplate.opsForValue().get(redisKey);
        if (rawData == null) {
            return null;
        }
        return (Url) rawData;
    }

}
