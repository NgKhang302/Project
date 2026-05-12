package com.songnhip24.news.controller;

import com.songnhip24.news.dto.LoginRequest;
import com.songnhip24.news.dto.LoginResponse;
import com.songnhip24.news.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // POST /api/auth/login
    // Body: { "username": "admin", "password": "123456" }
    // Response: { "token": "eyJ..." }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
