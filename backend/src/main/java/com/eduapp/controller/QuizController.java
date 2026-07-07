package com.eduapp.controller;

import com.eduapp.dto.QuizAnswerRequest;
import com.eduapp.dto.QuizAnswerResponse;
import com.eduapp.dto.QuizResponse;
import com.eduapp.service.QuizService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/lesson/{lessonId}")
    public List<QuizResponse> getByLesson(@PathVariable Long lessonId) {
        return quizService.getByLesson(lessonId, false);
    }

    @PostMapping("/submit")
    public QuizAnswerResponse submitAnswer(@Valid @RequestBody QuizAnswerRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        return quizService.submitAnswer(userId, request);
    }
}
