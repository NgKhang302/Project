package com.khang;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Tên scheme là "bearerAuth" (có thể đổi) — dùng chung cho toàn bộ API
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ))
                // Áp dụng bảo mật global lên tất cả endpoint
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }
}
