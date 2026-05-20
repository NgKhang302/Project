package com.khang.repository;
// truy cập database cho category
import com.khang.model.Category;
import org.springframework.data.jpa.repository.JpaRepository; //cấp full CRUD

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
