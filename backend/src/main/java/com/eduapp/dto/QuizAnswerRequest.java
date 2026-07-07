package com.eduapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizAnswerRequest {

    @NotNull(message = "Quiz is required")
    private Long quizId;

    @NotBlank(message = "Answer is required")
    private String answer;
}
