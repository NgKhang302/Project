package com.eduapp.service;

import com.eduapp.dto.WritingSubmissionRequest;
import com.eduapp.dto.WritingSubmissionResponse;
import com.eduapp.exception.ResourceNotFoundException;
import com.eduapp.exception.UnauthorizedException;
import com.eduapp.model.Lesson;
import com.eduapp.model.User;
import com.eduapp.model.WritingSubmission;
import com.eduapp.repository.LessonRepository;
import com.eduapp.repository.UserRepository;
import com.eduapp.repository.WritingSubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class WritingService {

    private static final int TARGET_WORD_COUNT = 30;
    private static final Pattern SENTENCE_SPLIT = Pattern.compile("[.!?]+");

    private final WritingSubmissionRepository writingSubmissionRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final LessonService lessonService;

    public WritingService(WritingSubmissionRepository writingSubmissionRepository,
                           LessonRepository lessonRepository,
                           UserRepository userRepository,
                           LessonService lessonService) {
        this.writingSubmissionRepository = writingSubmissionRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.lessonService = lessonService;
    }

    public WritingSubmissionResponse submit(Long userId, WritingSubmissionRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found: " + request.getLessonId()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        String content = request.getContent().trim();
        int wordCount = content.isEmpty() ? 0 : content.split("\\s+").length;

        WritingSubmission submission = WritingSubmission.builder()
                .user(user)
                .lesson(lesson)
                .content(content)
                .wordCount(wordCount)
                .build();
        writingSubmissionRepository.save(submission);

        lessonService.markCompleted(userId, lesson.getId(), null);

        return toResponse(submission);
    }

    public List<WritingSubmissionResponse> getHistory(Long userId, Long lessonId) {
        return writingSubmissionRepository.findByUserIdAndLessonIdOrderBySubmittedAtDesc(userId, lessonId)
                .stream().map(this::toResponse).toList();
    }

    private String buildFeedback(String content, int wordCount) {
        if (wordCount == 0) {
            return "Bài viết trống. Hãy thử viết ít nhất vài câu.";
        }
        int sentenceCount = (int) SENTENCE_SPLIT.splitAsStream(content).filter(s -> !s.isBlank()).count();
        StringBuilder feedback = new StringBuilder();
        feedback.append(wordCount).append(" từ, khoảng ").append(Math.max(sentenceCount, 1)).append(" câu. ");
        if (wordCount < TARGET_WORD_COUNT) {
            feedback.append("Hãy thử viết dài hơn một chút (mục tiêu khoảng ").append(TARGET_WORD_COUNT).append(" từ) để luyện diễn đạt ý tưởng đầy đủ hơn.");
        } else {
            feedback.append("Độ dài tốt! Hãy đọc lại để kiểm tra ngữ pháp và dấu câu trước khi hoàn thiện.");
        }
        return feedback.toString();
    }

    private WritingSubmissionResponse toResponse(WritingSubmission submission) {
        return WritingSubmissionResponse.builder()
                .id(submission.getId())
                .lessonId(submission.getLesson().getId())
                .content(submission.getContent())
                .wordCount(submission.getWordCount())
                .feedback(buildFeedback(submission.getContent(), submission.getWordCount()))
                .submittedAt(submission.getSubmittedAt())
                .build();
    }
}
