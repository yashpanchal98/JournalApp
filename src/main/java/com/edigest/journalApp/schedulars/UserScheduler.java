package com.edigest.journalApp.schedulars;

import com.edigest.journalApp.entity.JournalEntry;
import com.edigest.journalApp.entity.User;
import com.edigest.journalApp.enums.Sentiment;
import com.edigest.journalApp.repository.UserRepositoryImpl;
import com.edigest.journalApp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;


    //    @Scheduled(cron = "0 0 9 * * SUN")
//    @Scheduled(cron = "0 * * ? * *")
    public void fetchUserForSAandSendEmail() {

        List<User> list = userRepository.findUserForSA();
        System.out.println("Users fetched: " + list.size());
        for (User user : list) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            System.out.println("JournalEntries: " + user.getJournalEntries());
            List<Sentiment> sentiments = user.getJournalEntries().stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(20, ChronoUnit.DAYS))).map(x -> x.getSentiment()).collect(Collectors.toList());
            System.out.println("USER EMAIL = [" + user.getEmail() + "]");

            if (sentiments != null && !sentiments.isEmpty()) {
                HashMap<Sentiment, Integer> sentimentCount = new HashMap<>();

                for (Sentiment s : sentiments) {
                    if (s != null) {  // ignore null sentiments
                        sentimentCount.put(s, sentimentCount.getOrDefault(s, 0) + 1);
                    }
                }

                Sentiment mostFrequent = null;
                int maxCount = 0;
                for (Map.Entry<Sentiment, Integer> entry : sentimentCount.entrySet()) {
                    if (entry.getValue() > maxCount) {
                        maxCount = entry.getValue();
                        mostFrequent = entry.getKey();
                    }
                }

                System.out.println("Most frequent sentiment: " + mostFrequent);

                System.out.println("Most Frequent Sentiment = " + mostFrequent);
                if (mostFrequent != null) {
                    emailService.sendEmail(user.getEmail(), "Sentiment analysis for last 7 days", mostFrequent.toString());
                }
            }
        }
    }
}
