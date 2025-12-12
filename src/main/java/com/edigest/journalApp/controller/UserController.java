package com.edigest.journalApp.controller;

import com.edigest.journalApp.api_response.WeatherResponse;
import com.edigest.journalApp.entity.User;
//import com.edigest.journalApp.service.JournalEntryService;
import com.edigest.journalApp.service.UserService;
import com.edigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

//import static com.edigest.journalApp.service.UserService.passwordEncoder;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;


//    edit user details
    @PutMapping
    public void updateUser(@RequestBody User user) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User userInDb = userService.findByUsername(username);

        if(userInDb == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        userInDb.setUsername(user.getUsername());
        userInDb.setPassword(user.getPassword());
        userService.saveNewUser(userInDb);

    }

//    Delete user
    @DeleteMapping
    public void deleteUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User userInDb = userService.findByUsername(username);
        if(userInDb == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userService.deleteByUsername(username);
    }

//    get mapping for external api integration
    @GetMapping
    public String greet() {

        // Get logged-in username
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        WeatherResponse weather = weatherService.getWeatherByCity("Mumbai");

        if (weather == null || weather.getMain() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Weather not found");
        }

        // Extract temp & description
        double temp = weather.getMain().getTemp();
        String description = weather.getWeather().get(0).getDescription();

        // Build final message
        return "Hi " + username +
                ", current weather in Mumbai is " +
                temp + "°C (" + description + ")";
    }
}
