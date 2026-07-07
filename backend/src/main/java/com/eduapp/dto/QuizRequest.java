package com.eduapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizRequest {

    @NotNull(message = "Lesson is required")
    private Long lessonId;

    @NotBlank(message = "Question is required")
    private String question;

    @NotEmpty(message = "Options are required")
    private List<String> options;

    @NotBlank(message = "Correct answer is required")
    private String correctAnswer;

    private String explanation;
}
