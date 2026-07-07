package com.eduapp.controller;

import com.eduapp.dto.LoginRequest;
import com.eduapp.dto.LoginResponse;
import com.eduapp.dto.RegisterRequest;
import com.eduapp.dto.UserDTO;
import com.eduapp.exception.UnauthorizedException;
import com.eduapp.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final String cookieName;
    private final long cookieMaxAgeSeconds;

    public AuthController(AuthService authService,
                           @Value("${jwt.cookie-name}") String cookieName,
                           @Value("${jwt.expiration-ms}") long expirationMs) {
        this.authService = authService;
        this.cookieName = cookieName;
        this.cookieMaxAgeSeconds = expirationMs / 1000;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthService.AuthResult result = authService.login(request);

        ResponseCookie cookie = ResponseCookie.from(cookieName, result.token())
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(cookieMaxAgeSeconds)
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(LoginResponse.builder().user(result.user()).message("Login successful").build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).build();
    }

    @GetMapping("/check")
    public ResponseEntity<UserDTO> check(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        if (username == null) {
            throw new UnauthorizedException("Not authenticated");
        }
        return ResponseEntity.ok(authService.getCurrentUser(username));
    }
}
