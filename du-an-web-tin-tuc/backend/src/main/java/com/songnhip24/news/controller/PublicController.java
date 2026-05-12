package com.songnhip24.news.controller;

import com.songnhip24.news.dto.ArticleResponse;
import com.songnhip24.news.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final ArticleService service;

    public PublicController(ArticleService service) {
        this.service = service;
    }

    // GET /api/public/articles — homepage: danh sách bài đã published
    @GetMapping("/articles")
    public List<ArticleResponse> list() {
        return service.getPublished();
    }

    // GET /api/public/articles/{slug} — trang detail bài viết
    @GetMapping("/articles/{slug}")
    public ArticleResponse detail(@PathVariable String slug) {
        return service.getBySlug(slug);
    }
}
