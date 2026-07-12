package com.eduapp.controller;

import com.eduapp.dto.*;
import com.eduapp.service.CategoryService;
import com.eduapp.service.DialogueLineService;
import com.eduapp.service.FileStorageService;
import com.eduapp.service.LessonService;
import com.eduapp.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final CategoryService categoryService;
    private final LessonService lessonService;
    private final QuizService quizService;
    private final FileStorageService fileStorageService;
    private final DialogueLineService dialogueLineService;

    public AdminController(CategoryService categoryService, LessonService lessonService, QuizService quizService,
                            FileStorageService fileStorageService, DialogueLineService dialogueLineService) {
        this.categoryService = categoryService;
        this.lessonService = lessonService;
        this.quizService = quizService;
        this.fileStorageService = fileStorageService;
        this.dialogueLineService = dialogueLineService;
    }

    // --- Categories ---

    @GetMapping("/categories")
    public List<CategoryResponse> getCategories() {
        return categoryService.getAll();
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
    }

    @PutMapping("/categories/{id}")
    public CategoryResponse updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return categoryService.update(id, request);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- Lessons ---

    @GetMapping("/lessons")
    public List<LessonResponse> getLessons(@RequestParam Long categoryId) {
        return lessonService.getByCategory(categoryId);
    }

    @PostMapping("/lessons")
    public ResponseEntity<LessonResponse> createLesson(@Valid @RequestBody LessonRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.create(request));
    }

    @PutMapping("/lessons/{id}")
    public LessonResponse updateLesson(@PathVariable Long id, @Valid @RequestBody LessonRequest request) {
        return lessonService.update(id, request);
    }

    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload/audio")
    public Map<String, String> uploadAudio(@RequestParam("file") MultipartFile file) {
        return Map.of("url", fileStorageService.storeAudio(file));
    }

    @GetMapping("/lessons/{id}/dialogue-lines")
    public List<DialogueLineResponse> getDialogueLines(@PathVariable Long id) {
        return dialogueLineService.getByLesson(id);
    }

    @PutMapping("/lessons/{id}/dialogue-lines")
    public List<DialogueLineResponse> saveDialogueLines(@PathVariable Long id,
                                                          @RequestBody List<DialogueLineRequest> lines) {
        return dialogueLineService.replaceAll(id, lines);
    }

    // --- Quizzes ---

    @GetMapping("/quizzes")
    public List<QuizResponse> getQuizzes(@RequestParam Long lessonId) {
        return quizService.getByLesson(lessonId, true);
    }

    @PostMapping("/quizzes")
    public ResponseEntity<QuizResponse> createQuiz(@Valid @RequestBody QuizRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizService.create(request));
    }

    @PutMapping("/quizzes/{id}")
    public QuizResponse updateQuiz(@PathVariable Long id, @Valid @RequestBody QuizRequest request) {
        return quizService.update(id, request);
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
