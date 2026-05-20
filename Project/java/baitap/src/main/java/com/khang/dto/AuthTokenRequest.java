package com.khang.dto;
// Nhận username từ client để phục vụ quá trình tạo JWT token
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request body to generate a JWT token")
public class AuthTokenRequest {

    @Schema(example = "admin")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
