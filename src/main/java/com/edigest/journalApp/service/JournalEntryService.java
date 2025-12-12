package com.edigest.journalApp.service;

import com.edigest.journalApp.entity.JournalEntry;
import com.edigest.journalApp.entity.User;
import com.edigest.journalApp.repository.JournalEntryRepository;
import com.edigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


//    save entry
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            User user = userRepository.findByUsername(userName);
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userRepository.save(user);
        } catch (Exception ex) {
            throw new RuntimeException("An error occurred while entering the mew journal entry");
        }
    }

    //    save entry
    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    //    get all
    public List<JournalEntry> getAll(){
       return journalEntryRepository.findAll();
    }

//    get by Id
    public JournalEntry getEntryById(ObjectId id) {
        return journalEntryRepository.findById(id).get();
    }

//    delete
    @Transactional
    public boolean deleteEntryById(ObjectId id, String userName) {
        try {
            User user = userRepository.findByUsername(userName);
            boolean removed = user.getJournalEntries().removeIf(journalEntry -> journalEntry.getId().equals(id));
            if(removed) {
                userService.saveEntry(user);
                journalEntryRepository.deleteById(id);
            }
        } catch(Exception ex) {
            throw new RuntimeException("An error occurred while entering the mew journal entry");
        }
        return true;
    }

//    update entry


}
