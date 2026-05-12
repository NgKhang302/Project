package com.songnhip24.news.repository;

import com.songnhip24.news.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    // Lấy bài đã published, mới nhất lên đầu (homepage)
    List<Article> findByStatusOrderByPublishedAtDesc(String status);

    // Tìm bài theo slug (public detail)
    Optional<Article> findBySlug(String slug);

    // Kiểm tra slug đã tồn tại chưa
    boolean existsBySlug(String slug);
}
