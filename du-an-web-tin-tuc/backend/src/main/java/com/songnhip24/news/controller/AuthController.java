package com.songnhip24.news.controller;

import com.songnhip24.news.dto.LoginRequest;
import com.songnhip24.news.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController //convert response → JSON
@RequestMapping("/api/auth")
public class AuthController {

    private static final int COOKIE_MAX_AGE = 86400; // 24h

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(  //response trả json  "role": "ADMIN"
            @RequestBody LoginRequest request,//json LoginRequest object
            HttpServletResponse response) {  //gắn cookie vào response
        String token = authService.loginAndGetToken(request);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true); // k cho js đọc
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE);
        response.addCookie(cookie); //Tự động lưu cookie sẽ gửi kèm mỗi request sau

        return ResponseEntity.ok(Map.of("message", "Login successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", ""); // cái rỗng cho cái mới
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("message", "Logged out"));
    }

    @GetMapping("/check") //config jwtfilter
    public ResponseEntity<Map<String, String>> check() {
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
