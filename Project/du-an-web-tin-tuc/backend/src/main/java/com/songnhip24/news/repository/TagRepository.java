package com.songnhip24.news.repository;

import com.songnhip24.news.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
