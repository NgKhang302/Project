package com.eduapp.service;

import com.eduapp.dto.CategoryRequest;
import com.eduapp.dto.CategoryResponse;
import com.eduapp.exception.ResourceNotFoundException;
import com.eduapp.exception.ValidationException;
import com.eduapp.model.Category;
import com.eduapp.repository.CategoryRepository;
import com.eduapp.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final LessonRepository lessonRepository;

    public CategoryService(CategoryRepository categoryRepository, LessonRepository lessonRepository) {
        this.categoryRepository = categoryRepository;
        this.lessonRepository = lessonRepository;
    }

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream().map(this::toResponse).toList();
    }

    public CategoryResponse getById(Long id) {
        return toResponse(findById(id));
    }

    public CategoryResponse create(CategoryRequest request) {
        String slug = resolveSlug(request);
        if (categoryRepository.existsBySlug(slug)) {
            throw new ValidationException("A category with this slug already exists");
        }

        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .build();

        return toResponse(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = findById(id);
        String slug = resolveSlug(request);

        if (!slug.equals(category.getSlug()) && categoryRepository.existsBySlug(slug)) {
            throw new ValidationException("A category with this slug already exists");
        }

        category.setName(request.getName());
        category.setSlug(slug);
        category.setDescription(request.getDescription());

        return toResponse(categoryRepository.save(category));
    }

    public void delete(Long id) {
        Category category = findById(id);
        categoryRepository.delete(category);
    }

    private Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }

    private String resolveSlug(CategoryRequest request) {
        String base = (request.getSlug() != null && !request.getSlug().isBlank())
                ? request.getSlug()
                : request.getName();
        return slugify(base);
    }

    private String slugify(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutAccents = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalized).replaceAll("");
        return withoutAccents.toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .lessonCount(lessonRepository.findByCategoryId(category.getId()).size())
                .build();
    }
}
