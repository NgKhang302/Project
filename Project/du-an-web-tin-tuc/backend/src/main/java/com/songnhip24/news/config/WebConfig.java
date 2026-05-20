package com.songnhip24.news.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
//Serve file ảnh  là cầu nói URL với file thật
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Cho phép truy cập ảnh tại /uploads/ten-file.jpg
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("uploads");
        String uploadPath = uploadDir.toFile().getAbsolutePath();
// Chuyển sang đường dẫn tuyệt đối   /home/user/Projects/news/uploads/avatar.png"

        registry.addResourceHandler("/uploads/**")
                //Request đến /uploads/,,,,,,

                .addResourceLocations("file:" + uploadPath + "/");
    }                        // → Tìm file tại đây
}