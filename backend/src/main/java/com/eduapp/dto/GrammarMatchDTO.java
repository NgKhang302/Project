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
public class GrammarMatchDTO {
    private int offset;
    private int length;
    private String message;
    private String shortMessage;
    private String ruleId;
    private String category;
    private List<String> replacements;
}
