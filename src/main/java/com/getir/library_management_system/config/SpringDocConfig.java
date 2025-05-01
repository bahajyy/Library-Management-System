package com.getir.library_management_system.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * configuration for API documentation.
 */
@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI libraryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library Management System API")
                        .description("API documentation for the Patikadev Getir Bootcamp final project")
                        .version("1.0"));
    }
}
