package com.arekalov.notes.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.Components
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Notes API")
                    .version("1.0.0")
                    .description("REST API for simple notes with JWT authorization")
                    .contact(
                        Contact()
                            .name("arekalov")
                            .email("artyom.rekalov@gmail.com")
                    )
            )
            .addSecurityItem(SecurityRequirement().addList("bearerAuth"))
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearerAuth",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("Enter JWT token in format: Bearer <token>")
                    )
            )
    }
}
