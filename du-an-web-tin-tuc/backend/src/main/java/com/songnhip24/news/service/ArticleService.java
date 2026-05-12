package com.songnhip24.news.service;

import com.songnhip24.news.dto.ArticleRequest;
import com.songnhip24.news.dto.ArticleResponse;
import com.songnhip24.news.model.Article;
import com.songnhip24.news.model.Category;
import com.songnhip24.news.model.User;
import com.songnhip24.news.repository.ArticleRepository;
import com.songnhip24.news.repository.CategoryRepository;
import com.songnhip24.news.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository,
                          CategoryRepository categoryRepository,
                          UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // Admin: lấy tất cả bài (cả draft lẫn published)
    public List<ArticleResponse> getAll() {
        return articleRepository.findAll().stream()
                .map(ArticleResponse::from)
                .toList();
    }

    // Admin: lấy 1 bài theo id
    public ArticleResponse getById(Integer id) {
        return ArticleResponse.from(findById(id));
    }

    // Tạo bài mới, mặc định status = DRAFT
    public ArticleResponse create(ArticleRequest request, String username) {
        validate(request);
        if (articleRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug already exists: " + request.getSlug());
        }

        User author = findUser(username);
        Category category = findCategory(request.getCategoryId());

        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setSlug(request.getSlug());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setCoverImageUrl(request.getCoverImageUrl());
        article.setStatus("DRAFT");
        article.setCategory(category);
        article.setCreatedBy(author);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        return ArticleResponse.from(articleRepository.save(article));
    }

    // Cập nhật nội dung bài (vẫn giữ nguyên status)
    public ArticleResponse update(Integer id, ArticleRequest request) {
        validate(request);
        Article article = findById(id);

        if (!article.getSlug().equals(request.getSlug()) && articleRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug already exists: " + request.getSlug());
        }

        article.setTitle(request.getTitle());
        article.setSlug(request.getSlug());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setCoverImageUrl(request.getCoverImageUrl());
        article.setCategory(findCategory(request.getCategoryId()));
        article.setUpdatedAt(LocalDateTime.now());

        return ArticleResponse.from(articleRepository.save(article));
    }

    // Publish bài: đổi status → PUBLISHED, ghi publishedAt
    public ArticleResponse publish(Integer id) {
        Article article = findById(id);
        article.setStatus("PUBLISHED");
        article.setPublishedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        return ArticleResponse.from(articleRepository.save(article));
    }

    // Đưa về draft
    public ArticleResponse unpublish(Integer id) {
        Article article = findById(id);
        article.setStatus("DRAFT");
        article.setPublishedAt(null);
        article.setUpdatedAt(LocalDateTime.now());
        return ArticleResponse.from(articleRepository.save(article));
    }

    public void delete(Integer id) {
        findById(id);
        articleRepository.deleteById(id);
    }

    // Public: chỉ lấy bài đã published
    public List<ArticleResponse> getPublished() {
        return articleRepository.findByStatusOrderByPublishedAtDesc("PUBLISHED").stream()
                .map(ArticleResponse::from)
                .toList();
    }

    // Public: lấy bài theo slug
    public ArticleResponse getBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("Article not found: " + slug));
        if (!"PUBLISHED".equals(article.getStatus())) {
            throw new NoSuchElementException("Article not found: " + slug);
        }
        return ArticleResponse.from(article);
    }

    private Article findById(Integer id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Article not found: " + id));
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + username));
    }

    private Category findCategory(Integer categoryId) {
        if (categoryId == null) throw new IllegalArgumentException("Category id is required");
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + categoryId));
    }

    private void validate(ArticleRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (request.getSlug() == null || request.getSlug().isBlank()) {
            throw new IllegalArgumentException("Slug is required");
        }
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("Content is required");
        }
    }
}
