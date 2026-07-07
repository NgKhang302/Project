package com.eduapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WritingSubmissionResponse {
    private Long id;
    private Long lessonId;
    private String content;
    private int wordCount;
    private String feedback;
    private LocalDateTime submittedAt;
}
