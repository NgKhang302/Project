package com.eduapp.dto;

import com.eduapp.model.ProgressStatus;
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
public class UserProgressDTO {
    private Long id;
    private Long lessonId;
    private String lessonTitle;
    private ProgressStatus status;
    private Double score;
    private LocalDateTime completedAt;
}
