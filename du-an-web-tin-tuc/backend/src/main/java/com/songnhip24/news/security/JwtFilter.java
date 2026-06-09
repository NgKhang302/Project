package com.songnhip24.news.security;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JwtFilter implements Filter { //Polymorphism có thể dùng ở các file khác

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);  // tất cả instance dùng chung logger
    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override //gi đè method từ filter interface
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)   //chain.dofilter để cho req đi tiếp
            throws IOException, ServletException {                        // req tt từ clinet(cookie,url,....)
  //cast để dùng HTTP-specific methods vì servlet k có http-specific methods
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
    //servlet nhận req gửi res
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {  // hỏi sever cho get k
            chain.doFilter(req, res);  // skip JWT check
            return;
        }

        String token = extractTokenFromCookie(request);

        if (token == null) {
            sendUnauthorized(response, "Missing authentication cookie");
            return;
        }

        if (!jwtService.validate(token)) {
            log.warn("JWT invalid for path {}", request.getRequestURI()); //đường dẫn req
            sendUnauthorized(response, "Invalid or expired token");
            return;
        }

        request.setAttribute("username", jwtService.getUsername(token));
        chain.doFilter(req, res);  // gửi tiếp tới controll
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {  //Loop qua từng cookie
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
