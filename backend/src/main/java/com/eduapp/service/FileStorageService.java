package com.eduapp.service;

import com.eduapp.exception.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_AUDIO_TYPES = Set.of(
            "audio/mpeg", "audio/mp3", "audio/wav", "audio/ogg", "audio/x-wav", "audio/webm"
    );
    private static final long MAX_AUDIO_BYTES = 10L * 1024 * 1024;

    private final Path uploadDir;

    public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadDir = Path.of(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create upload directory", e);
        }
    }

    public String storeAudio(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("No file uploaded");
        }
        if (file.getSize() > MAX_AUDIO_BYTES) {
            throw new ValidationException("Audio file must be smaller than 10MB");
        }
        if (!ALLOWED_AUDIO_TYPES.contains(file.getContentType())) {
            throw new ValidationException("Unsupported audio format: " + file.getContentType());
        }

        String extension = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            extension = original.substring(original.lastIndexOf('.'));
        }
        String filename = UUID.randomUUID() + extension;

        try {
            Files.copy(file.getInputStream(), uploadDir.resolve(filename));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to store file", e);
        }

        return "/api/uploads/" + filename;
    }
}
