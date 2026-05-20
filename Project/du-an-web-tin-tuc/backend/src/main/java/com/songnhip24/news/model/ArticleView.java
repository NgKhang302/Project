package com.songnhip24.news.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "article_views")
public class ArticleView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(name = "viewed_at", nullable = false)
    private LocalDateTime viewedAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Article getArticle() { return article; }
    public void setArticle(Article article) { this.article = article; }

    public LocalDateTime getViewedAt() { return viewedAt; }
    public void setViewedAt(LocalDateTime viewedAt) { this.viewedAt = viewedAt; }
}
