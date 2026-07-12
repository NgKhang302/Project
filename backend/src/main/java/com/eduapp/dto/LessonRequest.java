package com.eduapp.dto;

import com.eduapp.model.CefrLevel;
import com.eduapp.model.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonRequest {

    @NotNull(message = "Category is required")
    private Long categoryId;

    @NotBlank(message = "Title is required")
    private String title;

    private String slug;

    private String content;

    @NotNull(message = "Content type is required")
    private ContentType contentType;

    private String audioUrl;

    private CefrLevel cefrLevel;
}
