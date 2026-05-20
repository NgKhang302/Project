package com.songnhip24.news.controller;

import com.songnhip24.news.dto.TagRequest;
import com.songnhip24.news.model.Tag;
import com.songnhip24.news.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {

    private final TagService service;

    public TagController(TagService service) {
        this.service = service;
    }

    @GetMapping("/api/public/tags")
    public List<Tag> listPublic() {
        return service.getAll();
    }

    @GetMapping("/api/admin/tags")
    public List<Tag> listAdmin() {
        return service.getAll();
    }

    @PostMapping("/api/admin/tags")
    public ResponseEntity<Tag> create(@RequestBody TagRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @DeleteMapping("/api/admin/tags/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
