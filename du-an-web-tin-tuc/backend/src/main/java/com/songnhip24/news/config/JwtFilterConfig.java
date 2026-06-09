package com.songnhip24.news.config;

import com.songnhip24.news.security.JwtFilter;
import com.songnhip24.news.security.JwtService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtFilterConfig {

    @Bean //inject
    public FilterRegistrationBean<JwtFilter> jwtFilter(JwtService jwtService) {
        FilterRegistrationBean<JwtFilter> reg = new FilterRegistrationBean<>();  //Tạo container bao bọc JwtFilter
        reg.setFilter(new JwtFilter(jwtService)); //JwtFilter vào container và gắn jwtservice vào constructor của jwtfilter
        reg.addUrlPatterns("/api/admin/*", "/api/auth/check");
        reg.setOrder(1);
        return reg;
    }
}