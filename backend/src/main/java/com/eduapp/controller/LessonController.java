package com.eduapp.controller;

import com.eduapp.dto.LessonResponse;
import com.eduapp.service.LessonService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("/{id}")
    public LessonResponse getLesson(@PathVariable Long id) {
        return lessonService.getById(id);
    }

    @PostMapping("/{id}/complete")
    public void completeLesson(@PathVariable Long id, HttpServletRequest request, @RequestBody(required = false) Map<String, Object> body) {
        Long userId = (Long) request.getAttribute("userId");
        Double score = body != null && body.get("score") != null ? Double.valueOf(body.get("score").toString()) : null;
        lessonService.markCompleted(userId, id, score);
    }
}
