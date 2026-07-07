package com.eduapp.controller;

import com.eduapp.dto.CategoryResponse;
import com.eduapp.dto.LessonResponse;
import com.eduapp.model.ContentType;
import com.eduapp.service.CategoryService;
import com.eduapp.service.LessonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final CategoryService categoryService;
    private final LessonService lessonService;

    public PublicController(CategoryService categoryService, LessonService lessonService) {
        this.categoryService = categoryService;
        this.lessonService = lessonService;
    }

    @GetMapping("/categories")
    public List<CategoryResponse> getCategories() {
        return categoryService.getAll();
    }

    @GetMapping("/lessons")
    public List<LessonResponse> getLessons(@RequestParam Long categoryId,
                                            @RequestParam(required = false) ContentType contentType) {
        return contentType == null
                ? lessonService.getByCategory(categoryId)
                : lessonService.getByCategoryAndType(categoryId, contentType);
    }

    @GetMapping("/lessons/{id}")
    public LessonResponse getLesson(@PathVariable Long id) {
        return lessonService.getById(id);
    }

    @GetMapping("/lessons/search")
    public List<LessonResponse> searchLessons(@RequestParam String q) {
        return lessonService.search(q);
    }
}
