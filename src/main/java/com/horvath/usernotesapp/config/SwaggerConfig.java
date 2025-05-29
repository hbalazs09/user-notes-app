package com.horvath.usernotesapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Notes Backend API")
                        .description("API documentation for the Notes application backend")
                        .version("1.0"));
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user")
                .pathsToMatch("/web/api/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi noteApi() {
        return GroupedOpenApi.builder()
                .group("note")
                .pathsToMatch("/web/api/note/**")
                .build();
    }
}