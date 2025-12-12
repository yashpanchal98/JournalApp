package com.edigest.journalApp.service;

import com.edigest.journalApp.api_response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;
    private final static String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=CITY&appid=API_KEY";

    @Autowired
    private RestTemplate restTemplate;

    public WeatherResponse getWeatherByCity(String city) {

        // Build the final URL
        String finalAPI = API_URL
                .replace("CITY", city)
                .replace("API_KEY", apiKey);

        // Call API
        ResponseEntity<WeatherResponse> response =
                restTemplate.getForEntity(finalAPI, WeatherResponse.class);

        return response.getBody();
    }
}
