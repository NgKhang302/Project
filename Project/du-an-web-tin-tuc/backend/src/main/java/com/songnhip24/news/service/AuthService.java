package com.songnhip24.news.service;

import com.songnhip24.news.dto.LoginRequest;
import com.songnhip24.news.dto.LoginResponse;
import com.songnhip24.news.dto.RegisterRequest;
import com.songnhip24.news.model.User;
import com.songnhip24.news.repository.UserRepository;
import com.songnhip24.news.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!encoder.matches(request.getPassword(), user.getPasswordHash())) {  //so sánh pass 123213, xyz mã hóa
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtService.generate(user.getUsername());  // tạo jwt token
        return new LoginResponse(token);
    }

    public LoginResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {  //isPresent cheek xem đối tượng tồn tại hay ko
            throw new IllegalArgumentException("Username đã tồn tại");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(encoder.encode(request.getPassword()));
        user.setRole("ADMIN");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtService.generate(user.getUsername());
        return new LoginResponse(token);
    }
}
