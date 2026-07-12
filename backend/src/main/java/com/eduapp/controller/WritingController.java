package com.eduapp.controller;

import com.eduapp.dto.GrammarCheckResponse;
import com.eduapp.dto.WritingSubmissionRequest;
import com.eduapp.dto.WritingSubmissionResponse;
import com.eduapp.service.WritingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/writing")
public class WritingController {

    private final WritingService writingService;

    public WritingController(WritingService writingService) {
        this.writingService = writingService;
    }

    @PostMapping("/submit")
    public WritingSubmissionResponse submit(@Valid @RequestBody WritingSubmissionRequest request, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        return writingService.submit(userId, request);
    }

    @GetMapping("/lesson/{lessonId}")
    public List<WritingSubmissionResponse> getHistory(@PathVariable Long lessonId, HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        return writingService.getHistory(userId, lessonId);
    }

    @PostMapping("/check")
    public GrammarCheckResponse check(@Valid @RequestBody WritingSubmissionRequest request) {
        return writingService.checkOnly(request.getContent());
    }
}
