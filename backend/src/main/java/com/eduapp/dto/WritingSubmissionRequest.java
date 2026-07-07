package com.eduapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WritingSubmissionRequest {

    @NotNull(message = "Lesson is required")
    private Long lessonId;

    @NotBlank(message = "Content is required")
    private String content;
}
