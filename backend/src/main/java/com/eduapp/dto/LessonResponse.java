package com.eduapp.dto;

import com.eduapp.model.ContentType;
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
public class LessonResponse {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private String title;
    private String slug;
    private String content;
    private ContentType contentType;
    private LocalDateTime createdAt;
}
