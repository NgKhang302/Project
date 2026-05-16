package com.songnhip24.news.service;

import com.songnhip24.news.dto.ArticleRequest;
import com.songnhip24.news.dto.ArticleResponse;
import com.songnhip24.news.model.*;
import com.songnhip24.news.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ArticleViewRepository articleViewRepository;

    public ArticleService(ArticleRepository articleRepository,
                          CategoryRepository categoryRepository,
                          UserRepository userRepository,
                          TagRepository tagRepository,
                          ArticleViewRepository articleViewRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.articleViewRepository = articleViewRepository;
    }
//admin coi tất cả các bài
    public List<ArticleResponse> getAll() {
        return articleRepository.findAll().stream()
                .map(a -> ArticleResponse.from(a, articleViewRepository.countByArticleId(a.getId())))
                .toList();
    }
// 1 bài cụ thể
    public ArticleResponse getById(Integer id) {
        Article a = findById(id);
        return ArticleResponse.from(a, articleViewRepository.countByArticleId(a.getId()));
    }

    @Transactional  //nếu lõi dừng hủy tất cả
    public ArticleResponse create(ArticleRequest request, String username) {
        validate(request); // kiểm trả title slug content ko đc rỗng
        if (articleRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug already exists: " + request.getSlug()); //cheek slug
        }

        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setSlug(request.getSlug());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setCoverImageUrl(request.getCoverImageUrl());
        article.setMetaDescription(request.getMetaDescription());
        article.setStatus("DRAFT"); // sever tự set

        article.setCategory(findCategory(request.getCategoryId()));
        article.setCreatedBy(findUser(username));
        article.setTags(resolveTags(request.getTagIds()));

        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        return ArticleResponse.from(articleRepository.save(article), 0L); // trả response(save in db)
    }

    @Transactional
    public ArticleResponse update(Integer id, ArticleRequest request) {
        validate(request);
        Article article = findById(id); // tìm bài cũ

        if (!article.getSlug().equals(request.getSlug()) && articleRepository.existsBySlug(request.getSlug())) { //equals so sánh 2 nọi dung có dóng nhau k
            throw new IllegalArgumentException("Slug already exists: " + request.getSlug());  //slug trong db có trùng với slug data user mún cập nhật k && slug mới đó có trùng với slug cũ k
        }

        article.setTitle(request.getTitle());
        article.setSlug(request.getSlug());
        article.setSummary(request.getSummary());
        article.setContent(request.getContent());
        article.setCoverImageUrl(request.getCoverImageUrl());
        article.setMetaDescription(request.getMetaDescription());
        article.setCategory(findCategory(request.getCategoryId()));
        article.setTags(resolveTags(request.getTagIds()));
        article.setUpdatedAt(LocalDateTime.now());

        Article saved = articleRepository.save(article);  // lưu vào db
        return ArticleResponse.from(saved, articleViewRepository.countByArticleId(saved.getId())); // gộp bài viết+ số viesw -> trả về cho client
    }

    public ArticleResponse publish(Integer id) {
        Article article = findById(id);
        article.setStatus("PUBLISHED");
        article.setPublishedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        Article saved = articleRepository.save(article);
        return ArticleResponse.from(saved, 0L);    }

    public ArticleResponse unpublish(Integer id) {
        Article article = findById(id);
        article.setStatus("DRAFT");
        article.setPublishedAt(null);
        article.setUpdatedAt(LocalDateTime.now());
        Article saved = articleRepository.save(article);
        return ArticleResponse.from(saved, articleViewRepository.countByArticleId(saved.getId()));
    }

    public void delete(Integer id) {
        findById(id);
        articleRepository.deleteById(id);
    }
//reader coi bài đã đăng
    public List<ArticleResponse> getPublished() {
        return articleRepository.findByStatusOrderByPublishedAtDesc("PUBLISHED").stream()
                .map(a -> ArticleResponse.from(a, articleViewRepository.countByArticleId(a.getId())))
                .toList();
    }
//coi 1 bài cụ thể
    public List<ArticleResponse> getPublishedByCategory(String categorySlug) {
        Category category = categoryRepository.findBySlug(categorySlug)
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + categorySlug));
        return articleRepository.findByStatusAndCategoryOrderByPublishedAtDesc("PUBLISHED", category).stream()
                .map(a -> ArticleResponse.from(a, articleViewRepository.countByArticleId(a.getId())))
                .toList();
    }

    @Transactional
    public ArticleResponse getBySlug(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new NoSuchElementException("Article not found: " + slug));
        if (!"PUBLISHED".equals(article.getStatus())) {
            throw new NoSuchElementException("Article not found: " + slug);
        }
        ArticleView view = new ArticleView();
        view.setArticle(article);
        view.setViewedAt(LocalDateTime.now());
        articleViewRepository.save(view);

        long viewCount = articleViewRepository.countByArticleId(article.getId());
        return ArticleResponse.from(article, viewCount);
    }
//VALIDATE DATA
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

    private List<Tag> resolveTags(List<Integer> tagIds) {    //resolveTags tìm nhiều tags theo list IDs
        if (tagIds == null || tagIds.isEmpty()) return new ArrayList<>();
        return tagRepository.findAllById(tagIds);
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
//isBlank cheek có kí tự k, k care spaces, spaces dc xem là trống