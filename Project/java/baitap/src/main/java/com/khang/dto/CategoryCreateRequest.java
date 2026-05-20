package com.khang.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body to create a category") // Schema mô tả API trên web (Swagger UI)
public class CategoryCreateRequest {

    @Schema(example = "Dien thoai")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
