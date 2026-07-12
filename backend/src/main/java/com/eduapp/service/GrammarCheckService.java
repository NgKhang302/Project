package com.eduapp.service;

import com.eduapp.dto.GrammarCheckResponse;
import com.eduapp.dto.GrammarMatchDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GrammarCheckService {

    private static final int MAX_REPLACEMENTS = 5;

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.languagetool.url}")
    private String languageToolUrl;

    @Value("${app.languagetool.timeout-ms}")
    private long timeoutMs;

    public List<GrammarMatchDTO> check(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        try {
            String body = "text=" + java.net.URLEncoder.encode(text, StandardCharsets.UTF_8)
                    + "&language=en-US";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(languageToolUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .timeout(Duration.ofMillis(timeoutMs))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return Collections.emptyList();
            }

            return parseMatches(response.body());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<GrammarMatchDTO> parseMatches(String responseBody) {
        List<GrammarMatchDTO> results = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            for (JsonNode match : root.path("matches")) {
                List<String> replacements = new ArrayList<>();
                for (JsonNode replacement : match.path("replacements")) {
                    if (replacements.size() >= MAX_REPLACEMENTS) break;
                    String value = replacement.path("value").asText(null);
                    if (value != null) replacements.add(value);
                }

                results.add(GrammarMatchDTO.builder()
                        .offset(match.path("offset").asInt())
                        .length(match.path("length").asInt())
                        .message(match.path("message").asText(null))
                        .shortMessage(match.path("shortMessage").asText(null))
                        .ruleId(match.path("rule").path("id").asText(null))
                        .category(match.path("rule").path("category").path("name").asText(null))
                        .replacements(replacements)
                        .build());
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }
        return results;
    }
}
