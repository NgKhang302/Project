package com.khang.dto;
//Dùng để trả JWT token về cho client sau khi xác thực thành công.
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT token response")
public class AuthTokenResponse {

    @Schema(example = "eyJhbGciOiJIUzM4NCJ9...")
    private final String token;

    public AuthTokenResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}