package com.songnhip24.news.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // cấu hình các file ảnh
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("uploads");//đường dãn tương đối
        String uploadPath = uploadDir.toFile().getAbsolutePath();
// Chuyển sang đường dẫn tuyệt đối   /home/user/Projects/news/uploads/avatar.png"

        registry.addResourceHandler("/uploads/**")
                //thêm header

                .addResourceLocations("file:" + uploadPath + "/");
    }                        // Tìm file
}