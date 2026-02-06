package com.khang.security;
// file read  token và gắn username.
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter implements Filter {

    private final JwtService jwtService;

    // Constructor nhận JwtService
    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        // Ép kiểu để đọc header
        HttpServletRequest request = (HttpServletRequest) req;

        // Lấy header Authorization
        String authHeader = request.getHeader("Authorization");

        // Nếu header có và bắt đầu bằng "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Lấy token

            // Nếu token hợp lệ
            if (jwtService.validate(token)) {
                String username = jwtService.getUsername(token); // Lấy username
                request.setAttribute("username", username);     // Lưu vào request
            }
        }

        // Cho request tiếp tục đi
        chain.doFilter(req, res);
    }
}
