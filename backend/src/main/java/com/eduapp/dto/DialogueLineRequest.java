package com.eduapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DialogueLineRequest {

    @NotBlank(message = "Speaker is required")
    private String speaker;

    @NotBlank(message = "Text is required")
    private String text;
}
