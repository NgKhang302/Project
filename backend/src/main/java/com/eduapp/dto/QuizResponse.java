package com.eduapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizResponse {
    private Long id;
    private Long lessonId;
    private String question;
    private List<String> options;
    /** Only populated for admin views; omitted from public quiz-taking responses. */
    private String correctAnswer;
    private String explanation;
}
