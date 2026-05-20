package com.songnhip24.news.service;

import com.songnhip24.news.model.NewsletterSubscriber;
import com.songnhip24.news.repository.NewsletterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NewsletterService {

    private final NewsletterRepository repository;

    public NewsletterService(NewsletterRepository repository) {
        this.repository = repository;
    }

    public void subscribe(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already subscribed");
        }
        NewsletterSubscriber subscriber = new NewsletterSubscriber();
        subscriber.setEmail(email.toLowerCase().trim()); //trim là xóa spaces ở đầu và cúi '' ahah@ '' > ''ahah@''
        subscriber.setSubscribedAt(LocalDateTime.now());
        repository.save(subscriber);
    }
}
