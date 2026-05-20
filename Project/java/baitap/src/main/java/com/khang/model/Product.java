package com.khang.model;

import jakarta.persistence.*; // công dụng là dùng các @....

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Double price;

    private String image;

    @ManyToOne //mỗi product thuộc 1 category, còn 1 category có thể nhiều product.
    @JoinColumn(name = "category_id")  // tên trong cột bảng sẽ lưu id của category 
    private Category category;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public Category getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}