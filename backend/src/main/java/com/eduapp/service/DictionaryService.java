package com.eduapp.service;

import com.eduapp.dto.DictionaryResponse;
import com.eduapp.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictionaryService {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TranslationService translationService;

    public DictionaryService(TranslationService translationService) {
        this.translationService = translationService;
    }

    @Cacheable("dictionary")
    public DictionaryResponse lookup(String word) {
        String normalized = word.trim().toLowerCase();
        if (normalized.isEmpty()) {
            throw new ResourceNotFoundException("No definition found for: " + word);
        }

        String encodedWord = java.net.URLEncoder.encode(normalized, StandardCharsets.UTF_8);
        String requestUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + encodedWord;

        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .timeout(Duration.ofSeconds(8))
                    .GET()
                    .build();
            response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException("Dictionary lookup failed");
        }

        if (response.statusCode() == 404) {
            throw new ResourceNotFoundException("No definition found for: " + normalized);
        }
        if (response.statusCode() != 200) {
            throw new RuntimeException("Dictionary lookup failed");
        }

        DictionaryResponse result = parse(response.body());
        result.setVietnameseTranslation(translationService.translate(normalized, "en", "vi"));
        return result;
    }

    private DictionaryResponse parse(String body) {
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode entry = root.isArray() && !root.isEmpty() ? root.get(0) : root;

            String word = entry.path("word").asText(null);
            String phonetic = entry.path("phonetic").asText(null);
            String phoneticAudioUrl = null;
            for (JsonNode phoneticNode : entry.path("phonetics")) {
                String audio = phoneticNode.path("audio").asText(null);
                if (audio != null && !audio.isBlank()) {
                    phoneticAudioUrl = audio;
                    if (phonetic == null || phonetic.isBlank()) {
                        phonetic = phoneticNode.path("text").asText(null);
                    }
                    break;
                }
            }

            List<DictionaryResponse.MeaningDTO> meanings = new ArrayList<>();
            for (JsonNode meaningNode : entry.path("meanings")) {
                List<DictionaryResponse.DefinitionDTO> definitions = new ArrayList<>();
                for (JsonNode defNode : meaningNode.path("definitions")) {
                    definitions.add(DictionaryResponse.DefinitionDTO.builder()
                            .definition(defNode.path("definition").asText(null))
                            .example(defNode.path("example").asText(null))
                            .build());
                }
                meanings.add(DictionaryResponse.MeaningDTO.builder()
                        .partOfSpeech(meaningNode.path("partOfSpeech").asText(null))
                        .definitions(definitions)
                        .build());
            }

            return DictionaryResponse.builder()
                    .word(word)
                    .phonetic(phonetic)
                    .phoneticAudioUrl(phoneticAudioUrl)
                    .meanings(meanings)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Dictionary lookup failed");
        }
    }
}
