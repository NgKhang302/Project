package com.khang.controller;

import com.khang.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtService jwt;
    public AuthController(JwtService jwt) { this.jwt = jwt; }

    //production must check password
    @PostMapping("/token")    // String ở đay la JWT token
    public ResponseEntity<String> token(@RequestParam String username) {
        String t = jwt.generate(username);
        return ResponseEntity.ok(t);
    }
}
//file Nhận username → gọi JwtService → trả token cho client
