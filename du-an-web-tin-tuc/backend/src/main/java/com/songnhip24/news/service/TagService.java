package com.songnhip24.news.service;

import com.songnhip24.news.dto.TagRequest;
import com.songnhip24.news.model.Tag;
import com.songnhip24.news.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TagService {

    private final TagRepository repository;

    public TagService(TagRepository repository) {
        this.repository = repository;
    }

    public List<Tag> getAll() {
        return repository.findAll();
    }

    public Tag create(TagRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Tag name is required");
        }
        if (request.getSlug() == null || request.getSlug().isBlank()) {
            throw new IllegalArgumentException("Tag slug is required");
        }
        if (repository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug already exists: " + request.getSlug());
        }
        Tag tag = new Tag();
        tag.setName(request.getName());
        tag.setSlug(request.getSlug());
        return repository.save(tag);
    }

    @Transactional
    public Tag update(Integer id, TagRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Tag name is required");
        }
        if (request.getSlug() == null || request.getSlug().isBlank()) {
            throw new IllegalArgumentException("Tag slug is required");
        }
        Tag tag = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tag not found: " + id));
        if (!tag.getSlug().equals(request.getSlug()) && repository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug already exists: " + request.getSlug());
        }
        tag.setName(request.getName());
        tag.setSlug(request.getSlug());
        return repository.save(tag);
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Tag not found: " + id);
        }
        repository.deleteById(id);
    }
}
