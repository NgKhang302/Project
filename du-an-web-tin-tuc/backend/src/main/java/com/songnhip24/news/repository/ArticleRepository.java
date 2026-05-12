package com.songnhip24.news.repository;

import com.songnhip24.news.model.Article;
import com.songnhip24.news.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findByStatusOrderByPublishedAtDesc(String status);
    List<Article> findByStatusAndCategoryOrderByPublishedAtDesc(String status, Category category);
    Optional<Article> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
