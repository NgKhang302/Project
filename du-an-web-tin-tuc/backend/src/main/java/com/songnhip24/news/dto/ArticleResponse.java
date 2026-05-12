package com.songnhip24.news.dto;

import com.songnhip24.news.model.Article;

import java.time.LocalDateTime;

// DTO tránh circular reference khi serialize entity có @ManyToOne
public class ArticleResponse {
    private Integer id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String status;
    private String coverImageUrl;
    private Integer categoryId;
    private String categoryName;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    public static ArticleResponse from(Article a) {
        ArticleResponse r = new ArticleResponse();
        r.id = a.getId();
        r.title = a.getTitle();
        r.slug = a.getSlug();
        r.summary = a.getSummary();
        r.content = a.getContent();
        r.status = a.getStatus();
        r.coverImageUrl = a.getCoverImageUrl();
        r.categoryId = a.getCategory().getId();
        r.categoryName = a.getCategory().getName();
        r.createdBy = a.getCreatedBy().getUsername();
        r.createdAt = a.getCreatedAt();
        r.updatedAt = a.getUpdatedAt();
        r.publishedAt = a.getPublishedAt();
        return r;
    }

    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public String getSlug() { return slug; }
    public String getSummary() { return summary; }
    public String getContent() { return content; }
    public String getStatus() { return status; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public Integer getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getCreatedBy() { return createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
}
