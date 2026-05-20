package com.khang.controller;

import com.khang.dto.CategoryCreateRequest;
import com.khang.model.Category;
import com.khang.service.CategoryService;
import org.springframework.web.bind.annotation.*;  //@....

import java.util.List;

@RestController  // trả json
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service){
        this.service = service;
    }

    @PostMapping
    public Category create(@RequestBody CategoryCreateRequest req){
        Category c = new Category();
        c.setName(req.getName());
        return service.create(c);
    }

    @GetMapping
    public List<Category> getAll(){
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        service.delete(id);
    }  //@p lấy id từ /asfdsfasdf/dfdsf..
}
