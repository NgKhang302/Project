package com.songnhip24.news.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// Chặn mọi request vào /api/admin/ kiểm tra Bearer token
// Nếu hợp lệ gắn username vào request để controller dùng
public class JwtFilter implements Filter {   // interface của java servlet

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class); //Console = Màn hình terminal khi chạy server là Log
    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override  //Ghi đè method doFilter() từ interface Filter
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
// ép kiểu dùng interface con để dùng được method HTTP
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // OPTIONS là preflight request của browser — cho qua không cần token
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(req, res); // cho request đi tiếp
            return;  // Browser hiện đại trước khi gửi request thật (POST, PUT...) sẽ tự gửi một request OPTIONS hỏi "server có cho phép không?" gọi là CORS preflight. Request này không có token nên phải cho qua, không thì frontend bị block ngay từ đầu.

        }
// lấy token từ header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            addCorsHeaders(request, response);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }
// lấy token cắt chữ Bearer và bỏ space đầu cúi
        String token = authHeader.substring(7).trim();

        if (!jwtService.validate(token)) {
            log.warn("JWT invalid for path {}", request.getRequestURI());
            addCorsHeaders(request, response);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");  //401
            return;
        }

        request.setAttribute("username", jwtService.getUsername(token));
        chain.doFilter(req, res);
    }

    private void addCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        if (origin != null) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }
    }
}