package com.songnhip24.news.config;

import com.songnhip24.news.security.JwtFilter;
import com.songnhip24.news.security.JwtService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtFilterConfig {

    @Bean  //Spring quản lý object này
    public FilterRegistrationBean<JwtFilter> jwtFilter(JwtService jwtService) {
        FilterRegistrationBean<JwtFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(new JwtFilter(jwtService));
        // Chỉ bảo vệ các route admin
        reg.addUrlPatterns("/api/admin/*");
        reg.setOrder(1);  // thứ tự ưu tiên
        return reg;
    }
}
// cấu hình cho Jwt filter chạy route api admin