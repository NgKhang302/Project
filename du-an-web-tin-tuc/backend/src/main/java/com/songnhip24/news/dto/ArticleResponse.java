package com.songnhip24.news.dto;

import com.songnhip24.news.model.Article;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleResponse {
    private Integer id;
    private String title;
    private String slug;
    private String summary;
    private String content;
    private String status;
    private String coverImageUrl;
    private String metaDescription;

    private Integer categoryId;
    private String categoryName;
    private String categorySlug;

    private String createdBy;
    private List<String> tags;
    private long viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    public static ArticleResponse from(Article a) {
        return from(a, 0L);  //lần 1 dùng get ở dứi cùng
        // [POLYMORPHISM — Method Overloading] gọi method bên dưới với viewCount mặc định 0
    }

    public static ArticleResponse from(Article a, long viewCount) {
        // [POLYMORPHISM — Overloading] cùng tên from(), khác tham số
        ArticleResponse r = new ArticleResponse(); // get update vào cái lần 2
        r.id = a.getId();
        r.title = a.getTitle();
        r.slug = a.getSlug();
        r.summary = a.getSummary();
        r.content = a.getContent();
        r.status = a.getStatus();
        r.coverImageUrl = a.getCoverImageUrl();
        r.metaDescription = a.getMetaDescription();

        r.categoryId = a.getCategory().getId();
        r.categoryName = a.getCategory().getName();
        r.categorySlug = a.getCategory().getSlug();

        r.createdBy = a.getCreatedBy().getUsername();
        // lấy username từ User object, không lộ passwordHash
        r.tags = a.getTags().stream().map(t -> t.getName()).toList();

        r.viewCount = viewCount; // = 0L từ lần 1
        r.createdAt = a.getCreatedAt();
        r.updatedAt = a.getUpdatedAt();
        r.publishedAt = a.getPublishedAt();
        return r; // lần 2 xog r trả return from(a, 0L);
    }

    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public String getSlug() { return slug; }
    public String getSummary() { return summary; }
    public String getContent() { return content; }
    public String getStatus() { return status; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public String getMetaDescription() { return metaDescription; }
    public Integer getCategoryId() { return categoryId; }
    public String getCategoryName() { return categoryName; }
    public String getCategorySlug() { return categorySlug; }
    public String getCreatedBy() { return createdBy; }
    public List<String> getTags() { return tags; }
    public long getViewCount() { return viewCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
}