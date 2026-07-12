package com.eduapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class TranslationService {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Translates short text via the unofficial Google Translate endpoint (no API key required),
     * matching the same approach already used for TTS in TtsController. Returns null on any
     * failure so callers can degrade gracefully instead of failing the whole request.
     */
    public String translate(String text, String sourceLang, String targetLang) {
        if (text == null || text.isBlank()) {
            return null;
        }

        try {
            String encodedText = java.net.URLEncoder.encode(text, StandardCharsets.UTF_8);
            String requestUrl = "https://translate.googleapis.com/translate_a/single?client=gtx"
                    + "&sl=" + sourceLang + "&tl=" + targetLang + "&dt=t&q=" + encodedText;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                            + "(KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(Duration.ofSeconds(6))
                    .GET()
                    .build();

            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return null;
            }

            return parseTranslation(response.body());
        } catch (Exception e) {
            return null;
        }
    }

    private String parseTranslation(String body) {
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode sentences = root.path(0);
            StringBuilder result = new StringBuilder();
            for (JsonNode sentence : sentences) {
                String chunk = sentence.path(0).asText(null);
                if (chunk != null) {
                    result.append(chunk);
                }
            }
            String translated = result.toString().trim();
            return translated.isEmpty() ? null : translated;
        } catch (Exception e) {
            return null;
        }
    }
}
