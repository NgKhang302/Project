package com.khang;
<<<<<<< HEAD
// flie test cấu hình trên Swagger
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
=======

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
>>>>>>> 4cd547e204fb619157b0947f82eae37cf6ff7748
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

<<<<<<< HEAD
@Configuration  //cấu hình Swagger cho toan project
=======
@Configuration
>>>>>>> 4cd547e204fb619157b0947f82eae37cf6ff7748
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
<<<<<<< HEAD
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .components(new Components() //security schema API config
                        .addSecuritySchemes(securitySchemeName, //hệ thống auth vào Swagger
=======
        // Tên scheme là "bearerAuth" (có thể đổi) — dùng chung cho toàn bộ API
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
>>>>>>> 4cd547e204fb619157b0947f82eae37cf6ff7748
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ))
<<<<<<< HEAD
                ; //Swagger hiểu API dùng JWT Bearer
=======
                // Áp dụng bảo mật global lên tất cả endpoint
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
>>>>>>> 4cd547e204fb619157b0947f82eae37cf6ff7748
    }
}
