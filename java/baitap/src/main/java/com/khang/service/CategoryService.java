package com.khang.service;

import com.khang.model.Category;
import com.khang.repository.CategoryRepository;
import org.springframework.stereotype.Service; //@....

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public Category create(Category c){
        if (c == null || c.getName() == null || c.getName().isBlank()) {  // Blank là chuỗi trống ("") hoặc khoảng trắng
            throw new IllegalArgumentException("Category name is required");
        }
        return repo.save(c);
    }

    public List<Category> getAll(){
        return repo.findAll();
    }

    public void delete(Integer id){
        repo.deleteById(id);
    }
}
