package com.songnhip24.news.service;

import com.songnhip24.news.dto.CategoryRequest;
import com.songnhip24.news.model.Category;
import com.songnhip24.news.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> getAll() {
        return repository.findAll();
    }

    public Category getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + id));
    }

    public Category create(CategoryRequest request) {
        validate(request);
        if (repository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug already exists: " + request.getSlug());
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(request.getDescription());
        return repository.save(category);
    }

    @Transactional
    public Category update(Integer id, CategoryRequest request) {
        validate(request);
        Category category = getById(id);

        // Cho phép giữ slug cũ, chỉ báo lỗi nếu slug mới đã tồn tại ở record khác
        if (!category.getSlug().equals(request.getSlug()) && repository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Slug already exists: " + request.getSlug());
        }

        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(request.getDescription());
        return repository.save(category);
    }

    public void delete(Integer id) {
        getById(id); // ném lỗi nếu không tồn tại
        repository.deleteById(id);
    }

    private void validate(CategoryRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Category name is required");
        }
        if (request.getSlug() == null || request.getSlug().isBlank()) {
            throw new IllegalArgumentException("Category slug is required");
        }
    }
}
