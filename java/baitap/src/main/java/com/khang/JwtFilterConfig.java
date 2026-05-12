package com.khang;
// đăng kí jwt cho string boot
import com.khang.security.JwtFilter;
import com.khang.security.JwtService;
import org.springframework.boot.web.servlet.FilterRegistrationBean; //đăng kí
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtFilterConfig {

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtService jwtService) {
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>();  //object đăng ký filter
        registration.setFilter(new JwtFilter(jwtService)); //gắn JwtFilter vào syxtem
        registration.addUrlPatterns("/api/*");
        registration.setOrder(1); // JwtFilter chạy đầu
        return registration;
    }
}
