package com.songnhip24.news.controller;

import com.songnhip24.news.dto.CategoryRequest;
import com.songnhip24.news.model.Category;
import com.songnhip24.news.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    // ── Public ──────────────────────────────────────────────
    // GET /api/public/categories
    @GetMapping("/api/public/categories")
    public List<Category> listPublic() {
        return service.getAll();
    }

    // ── Admin (yêu cầu Bearer token) ────────────────────────
    // GET /api/admin/categories
    @GetMapping("/api/admin/categories")
    public List<Category> listAdmin() {
        return service.getAll();
    }

    // POST /api/admin/categories
    @PostMapping("/api/admin/categories")
    public ResponseEntity<Category> create(@RequestBody CategoryRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    // PUT /api/admin/categories/{id}
    @PutMapping("/api/admin/categories/{id}")
    public ResponseEntity<Category> update(@PathVariable Integer id,
                                           @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    // DELETE /api/admin/categories/{id}
    @DeleteMapping("/api/admin/categories/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build(); //204 No Content + không có body
    }
}
