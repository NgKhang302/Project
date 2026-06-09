package com.songnhip24.news.controller;

import com.songnhip24.news.dto.ArticleResponse;
import com.songnhip24.news.dto.NewsletterRequest;
import com.songnhip24.news.service.ArticleService;
import com.songnhip24.news.service.NewsletterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final ArticleService articleService;
    private final NewsletterService newsletterService;

    public PublicController(ArticleService articleService, NewsletterService newsletterService) {
        this.articleService = articleService;
        this.newsletterService = newsletterService;
    }

    // Danh sách bài đã published (homepage)
    @GetMapping("/articles")
    public List<ArticleResponse> list() {
        return articleService.getPublished();
    }

    // Chi tiết bài viết theo slug (tự động tăng lượt xem)
    @GetMapping("/articles/{slug}")
    public ArticleResponse detail(@PathVariable String slug) {
        return articleService.getBySlug(slug);
    }  //getBySlug() tự động INSERT vào article_view

    // Bài viết theo chuyên mục
    @GetMapping("/categories/{slug}/articles")
    public List<ArticleResponse> byCategory(@PathVariable String slug) {
        return articleService.getPublishedByCategory(slug);
    }

    // Đăng ký nhận bản tin
    @PostMapping("/newsletter") // body dạng JSON đơn giản + control được status code
    public ResponseEntity<Map<String, String>> subscribe(@RequestBody NewsletterRequest request) {
        newsletterService.subscribe(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Đăng ký thành công!"));
    }
}
///Stream + Map = Transform từng article
/// ├─ Lần 1: Article1 → ArticleResponse1 (+ viewCount)
/// ├─ Lần 2: Article2 → ArticleResponse2 (+ viewCount)
/// ├─ Lần 3: Article3 → ArticleResponse3 (+ viewCount)
/// └─ Kết quả: List<ArticleResponse> (tất cả)///