package com.songnhip24.news.repository;

import com.songnhip24.news.model.ArticleView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleViewRepository extends JpaRepository<ArticleView, Integer> {
    long countByArticleId(Integer articleId);
}
