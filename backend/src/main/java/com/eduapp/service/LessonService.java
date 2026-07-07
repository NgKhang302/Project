package com.eduapp.service;

import com.eduapp.dto.LessonRequest;
import com.eduapp.dto.LessonResponse;
import com.eduapp.exception.ResourceNotFoundException;
import com.eduapp.exception.ValidationException;
import com.eduapp.model.Category;
import com.eduapp.model.ContentType;
import com.eduapp.model.Lesson;
import com.eduapp.model.ProgressStatus;
import com.eduapp.model.User;
import com.eduapp.model.UserProgress;
import com.eduapp.repository.CategoryRepository;
import com.eduapp.repository.LessonRepository;
import com.eduapp.repository.UserProgressRepository;
import com.eduapp.repository.UserRepository;
import com.eduapp.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CategoryRepository categoryRepository;
    private final UserProgressRepository userProgressRepository;
    private final UserRepository userRepository;

    public LessonService(LessonRepository lessonRepository,
                          CategoryRepository categoryRepository,
                          UserProgressRepository userProgressRepository,
                          UserRepository userRepository) {
        this.lessonRepository = lessonRepository;
        this.categoryRepository = categoryRepository;
        this.userProgressRepository = userProgressRepository;
        this.userRepository = userRepository;
    }

    public List<LessonResponse> getByCategory(Long categoryId) {
        return lessonRepository.findByCategoryId(categoryId).stream().map(this::toResponse).toList();
    }

    public List<LessonResponse> getByCategoryAndType(Long categoryId, ContentType type) {
        return lessonRepository.findByCategoryIdAndContentType(categoryId, type).stream().map(this::toResponse).toList();
    }

    public List<LessonResponse> search(String query) {
        return lessonRepository.findByTitleContainingIgnoreCase(query).stream().map(this::toResponse).toList();
    }

    public LessonResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public LessonResponse getBySlug(String slug) {
        Lesson lesson = lessonRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found: " + slug));
        return toResponse(lesson);
    }

    public LessonResponse create(LessonRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.getCategoryId()));

        String slug = resolveSlug(request);
        if (lessonRepository.existsBySlug(slug)) {
            throw new ValidationException("A lesson with this slug already exists");
        }

        Lesson lesson = Lesson.builder()
                .category(category)
                .title(request.getTitle())
                .slug(slug)
                .content(request.getContent())
                .contentType(request.getContentType())
                .audioUrl(request.getAudioUrl())
                .build();

        return toResponse(lessonRepository.save(lesson));
    }

    public LessonResponse update(Long id, LessonRequest request) {
        Lesson lesson = findById(id);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.getCategoryId()));

        String slug = resolveSlug(request);
        if (!slug.equals(lesson.getSlug()) && lessonRepository.existsBySlug(slug)) {
            throw new ValidationException("A lesson with this slug already exists");
        }

        lesson.setCategory(category);
        lesson.setTitle(request.getTitle());
        lesson.setSlug(slug);
        lesson.setContent(request.getContent());
        lesson.setContentType(request.getContentType());
        lesson.setAudioUrl(request.getAudioUrl());

        return toResponse(lessonRepository.save(lesson));
    }

    public void delete(Long id) {
        Lesson lesson = findById(id);
        lessonRepository.delete(lesson);
    }

    public void markCompleted(Long userId, Long lessonId, Double score) {
        Lesson lesson = findById(lessonId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        UserProgress progress = userProgressRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElseGet(() -> UserProgress.builder().user(user).lesson(lesson).build());

        progress.setStatus(ProgressStatus.COMPLETED);
        progress.setScore(score);
        progress.setCompletedAt(LocalDateTime.now());

        userProgressRepository.save(progress);
    }

    private Lesson findById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found: " + id));
    }

    private String resolveSlug(LessonRequest request) {
        String base = (request.getSlug() != null && !request.getSlug().isBlank())
                ? request.getSlug()
                : request.getTitle();
        return slugify(base);
    }

    private String slugify(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutAccents = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalized).replaceAll("");
        return withoutAccents.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }

    private LessonResponse toResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .categoryId(lesson.getCategory().getId())
                .categoryName(lesson.getCategory().getName())
                .title(lesson.getTitle())
                .slug(lesson.getSlug())
                .content(lesson.getContent())
                .contentType(lesson.getContentType())
                .audioUrl(lesson.getAudioUrl())
                .createdAt(lesson.getCreatedAt())
                .build();
    }
}
