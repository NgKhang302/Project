package com.songnhip24.news.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

// Chặn mọi request vào /api/admin/**, kiểm tra Bearer token
// Nếu hợp lệ → gắn username vào request để controller dùng
public class JwtFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // OPTIONS là preflight request của browser — cho qua không cần token
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(req, res);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7).trim();

        if (!jwtService.validate(token)) {
            log.warn("JWT invalid for path {}", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            return;
        }

        request.setAttribute("username", jwtService.getUsername(token));
        chain.doFilter(req, res);
    }
}