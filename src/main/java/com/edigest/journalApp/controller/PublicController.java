package com.edigest.journalApp.controller;
import com.edigest.journalApp.entity.User;
import com.edigest.journalApp.service.EmailService;
import com.edigest.journalApp.service.UserService;
import com.edigest.journalApp.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


// This is unauthenticated controller
@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/check-health")
    public String healthCheck() {
        return "OK";
    }


    @PostMapping("/sign-up")
    public void signup(@RequestBody User user) {
        userService.saveNewUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword()
                    )
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occured while createAuthenticationToken");
            return new ResponseEntity<>("Incorrect userName or Password",HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/mail")
    public String sendMail(@RequestBody Map<String, String> payload) {

        emailService.sendEmail(
                payload.get("to"),
                payload.get("subject"),
                payload.get("body")
        );

        return "Mail sent successfully";
    }

}
