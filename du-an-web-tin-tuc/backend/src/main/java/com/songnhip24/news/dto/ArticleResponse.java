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
        return from(a, 0L);
    } //- Danh sách bài viết (không cần đếm)- Công khai danh mục (không cần đếm)- Tìm kiếm (không cần đếm)

    public static ArticleResponse from(Article a, long viewCount) { //- Xem chi tiết bài viết (cần hiển thị lượt xem)- Trang bài viết (cần hiển thị lượt xem)
        ArticleResponse r = new ArticleResponse();
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

        r.tags = a.getTags().stream().map(t -> t.getName()).toList(); //Danh sách [Tag, Tag, Tag] còn stream là Dòng Tag → Tag → Tag
        r.viewCount = viewCount;

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
