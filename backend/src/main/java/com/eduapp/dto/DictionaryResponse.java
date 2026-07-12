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
public class DictionaryResponse {
    private String word;
    private String phonetic;
    private String phoneticAudioUrl;
    private String vietnameseTranslation;
    private List<MeaningDTO> meanings;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MeaningDTO {
        private String partOfSpeech;
        private List<DefinitionDTO> definitions;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DefinitionDTO {
        private String definition;
        private String example;
    }
}
