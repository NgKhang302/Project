package com.khang.repository;
// truy cập database cho Product.

import com.khang.model.Product;
import org.springframework.data.jpa.repository.JpaRepository; //tool CRUD

import java.util.List; // trả nhiều product
public interface ProductRepository extends JpaRepository<Product,Integer> {

    List<Product> findByNameContainingIgnoreCase(String name);

}