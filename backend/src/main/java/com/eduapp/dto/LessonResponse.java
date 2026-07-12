package com.eduapp.dto;

import com.eduapp.model.CefrLevel;
import com.eduapp.model.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    private String audioUrl;
    private CefrLevel cefrLevel;
    private LocalDateTime createdAt;
    private List<DialogueLineResponse> dialogueLines;
}
