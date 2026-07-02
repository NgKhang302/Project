package com.songnhip24.news.service;

import com.songnhip24.news.dto.LoginRequest;
import com.songnhip24.news.model.User;
import com.songnhip24.news.repository.UserRepository;
import com.songnhip24.news.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public String loginAndGetToken(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) { //isBlank "" "  "
            throw new IllegalArgumentException("Username is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!encoder.matches(request.getPassword(), user.getPasswordHash())) { // so sánh pass encode.matches
            throw new IllegalArgumentException("Invalid username or password");
        }

        return jwtService.generate(user.getUsername()); //tạo token
    }
}
