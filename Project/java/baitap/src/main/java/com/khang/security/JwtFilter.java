package com.khang.security;
<<<<<<< HEAD
// cheek req nếu đúng thì gắn username vào req
import jakarta.servlet.Filter;  //lọc
import jakarta.servlet.FilterChain; // cheek ok cho đi tiếp
import jakarta.servlet.ServletException; // các lỗi
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest; //req HTTP
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JwtFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class); // tool log cho jwtfilter

    private final JwtService jwtService;

=======
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
>>>>>>> 4cd547e204fb619157b0947f82eae37cf6ff7748
    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
<<<<<<< HEAD
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) //chạy khi req đi qua
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;   //biến request “thô” → chuyển thành HTTP request
        String authHeader = request.getHeader("Authorization");  //thành Bearer asfsafiwefhwih12443...

        if (authHeader != null && authHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
            String token = authHeader.substring(7).trim(); //cắt chữ Bearer
            if (jwtService.validate(token)) {
                request.setAttribute("username", jwtService.getUsername(token)); //đánh dấu req thuộc user nào đẩy xún product
            } else {
                log.warn("JWT validation failed for path {}", request.getRequestURI());
            }
        }

        chain.doFilter(req, res); //xog filter cho vào controll
=======
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
>>>>>>> 4cd547e204fb619157b0947f82eae37cf6ff7748
    }
}
