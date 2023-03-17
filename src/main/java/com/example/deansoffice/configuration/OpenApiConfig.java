package com.example.deansoffice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()) // Create a new Components object
                .info(new Info() // Set the API's info

                    // Set the API title
                    .title("Your API Title")

                    // Set the API version
                    .version("1.0.0")

                    // Set the API description
                    .description("Your API Description")

                    // Set the terms of service URL
                    .termsOfService("http://swagger.io/terms/")

                    // Set the API license
                    .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}