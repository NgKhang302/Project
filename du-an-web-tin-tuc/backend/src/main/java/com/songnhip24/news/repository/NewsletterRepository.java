package com.songnhip24.news.repository;

import com.songnhip24.news.model.NewsletterSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsletterRepository extends JpaRepository<NewsletterSubscriber, Integer> {
    boolean existsByEmail(String email);
}
