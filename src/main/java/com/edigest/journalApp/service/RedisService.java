package com.edigest.journalApp.service;

import com.edigest.journalApp.api_response.WeatherResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void set(String key, Object value, Long ttlSeconds) {
        try {
            String json = objectMapper.writeValueAsString(value); // Serialize
            redisTemplate.opsForValue().set(key, json, Duration.ofSeconds(ttlSeconds));
            System.out.println("Set in Redis: " + key);
        } catch (Exception e) {
            log.error("Error setting Redis key: " + key, e);
        }
    }

    public <T> T get(String key, Class<T> clazz) {
        try {
            String json = redisTemplate.opsForValue().get(key).toString(); // Get as String
            if (json == null) return null;
            System.out.println("Getting from Redis: " + key);
            return objectMapper.readValue(json, clazz); // Deserialize JSON
        } catch (Exception e) {
            log.error("Error getting Redis key: " + key, e);
            return null;
        }
    }
}
