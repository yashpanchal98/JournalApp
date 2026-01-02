package com.edigest.journalApp.service;

import com.edigest.journalApp.api_response.WeatherResponse;
import com.edigest.journalApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    RedisService redisService;

    public WeatherResponse getWeatherByCity(String city) {

        WeatherResponse weatherResponse = redisService.get("Weather_of_" + city, WeatherResponse.class);

        if(weatherResponse != null) {
            return weatherResponse;
        } else {
            // Build the final URL
            String finalAPI = appCache.appCache.get("WEATHER_API")
                    .replace("<city>", city)
                    .replace("<apiKey>", apiKey);

            // Call API
            ResponseEntity<WeatherResponse> response = restTemplate.getForEntity(finalAPI, WeatherResponse.class);

            WeatherResponse body = response.getBody();
            if(body != null) {
                redisService.set("Weather_of_"+ city, body,500l);
            }
            return body;
        }


    }
}
