package com.khang.controller;
<<<<<<< HEAD
//Nhận username từ client, tạo JWT token bằng JwtService và trả token về cho client để dùng truy cập API.
import com.khang.dto.AuthTokenRequest;
import com.khang.dto.AuthTokenResponse;
=======

>>>>>>> 4cd547e204fb619157b0947f82eae37cf6ff7748
import com.khang.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtService jwt;
    public AuthController(JwtService jwt) { this.jwt = jwt; }

<<<<<<< HEAD
    @PostMapping("/token")
    public ResponseEntity<AuthTokenResponse> token(@RequestBody AuthTokenRequest request) {
        if (request == null || request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        String token = jwt.generate(request.getUsername());
        return ResponseEntity.ok(new AuthTokenResponse(token)); //bọc token vào DTO response trả HTTP 200 OK
    }
}
=======
    //production must check password
    @PostMapping("/token")    // String ở đay la JWT token
    public ResponseEntity<String> token(@RequestParam String username) {
        String t = jwt.generate(username);
        return ResponseEntity.ok(t);
    }
}
//file Nhận username → gọi JwtService → trả token cho client
>>>>>>> 4cd547e204fb619157b0947f82eae37cf6ff7748
