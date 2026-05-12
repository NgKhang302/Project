package com.khang.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body to create a product")
public class ProductCreateRequest {

    @Schema(example = "ip19")
    private String name;

    @Schema(example = "110")
    private Double price;

    @Schema(example = "uploads/ip19.png")
    private String image;

    @Schema(example = "1", description = "Existing category id in PostgreSQL")
    private Integer categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
