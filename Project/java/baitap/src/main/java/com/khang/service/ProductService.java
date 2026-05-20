package com.khang.service;

import com.khang.dto.ProductCreateRequest;
import com.khang.model.Category;
import com.khang.model.Product;
import com.khang.repository.CategoryRepository;
import com.khang.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException; // ném lỗi nếu k tìm tháy db

@Service
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository repository, CategoryRepository categoryRepository){
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    // CREATE
    public Product create(ProductCreateRequest request, String username){
        // AUTH CHECK
        if (username == null || username.isBlank()) {
            throw new SecurityException("Missing or invalid bearer token"); //SecurityException = dừng vìkhông có quyền

        }
        if(!username.equals("admin")){  //equals  so sánh valuê
            throw new SecurityException("Only admin can create product");
        }

        if (request == null) {
            throw new IllegalArgumentException("Product body is required");
        }
        if(request.getName() == null || request.getName().isBlank()){
            throw new IllegalArgumentException("Product name is required");
        }
        if (request.getCategoryId() == null) {
            throw new IllegalArgumentException("Category id is required");
        }

        Category category = categoryRepository.findById(request.getCategoryId()).orElse(null);   // lấy category từ db
        if (category == null) {
            // Nếu không thấy category, ném exception
            throw new NoSuchElementException("Category not found: " + request.getCategoryId());
        } else {
            // Nếu thấy category, dùng
            // category đã được gán
        }
//Gán dữ liệu từ DTO + category vào Product entity
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setImage(request.getImage());
        product.setCategory(category);

        return repository.save(product);
    }

    // GET ALL
    public List<Product> getAll(){
        return repository.findAll();
    }

    // GET BY ID
    public Product getById(Integer id){
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found: " + id));
    }   //NoSuchElementException(...) → exception báo

    // DELETE
    public void delete(Integer id){
        repository.deleteById(id);
    }

    // SEARCH
    public List<Product> search(String name){
        return repository.findByNameContainingIgnoreCase(name);
    }
}    //Containing = “có chứa cái này” , IgnoreCase ko phân biệt hoa thường 
