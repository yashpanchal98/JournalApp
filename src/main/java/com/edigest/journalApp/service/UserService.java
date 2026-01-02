package com.edigest.journalApp.service;


import com.edigest.journalApp.controller.UserController;
import com.edigest.journalApp.entity.JournalEntry;
import com.edigest.journalApp.entity.User;
import com.edigest.journalApp.repository.JournalEntryRepository;
import com.edigest.journalApp.repository.UserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j // automatic create log instance similar like @getter @setter in Lombook
public class UserService {

//    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //    save entry
    public void saveEntry(User user) {
        userRepository.save(user);
    }

//    save new user
    public void saveNewUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Error occured hahahahaha");
            log.info("Error occured hahahahha");
            log.warn("Error occured hahahahhha");
            log.debug("Error occured hahahahahah");
            log.trace("Error occured hahahahahha");
        }

    }

//    save new admin
    public void saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }

    //  get all users
    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getEntryById(ObjectId id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean deleteEntryById(ObjectId id) {
        userRepository.deleteById(id);
        return true;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean deleteByUsername(String username) {
        userRepository.deleteByUsername(username);
        return true;

    }

}
