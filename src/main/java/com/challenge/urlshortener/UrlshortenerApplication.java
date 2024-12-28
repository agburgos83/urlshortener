package com.challenge.urlshortener;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class UrlshortenerApplication implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(UrlshortenerApplication.class, args);
    }

    @SuppressWarnings({ "null", "deprecation" })
    @Override
    public void run(String... args) {
        // Verifica si el perfil activo es "dev"
        if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
            redisTemplate.getConnectionFactory().getConnection().flushDb();
            System.out.println("Redis database flushed in development mode.");
        }
    }
}