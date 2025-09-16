package com.dev.agent.config;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Agent-Backend API",
                version = "1.0",
                description = "API para tratamento e envio de comandos para os agentes.",
                contact = @Contact(
                        name = "Wesley",
                        email = "wesley.correa@vrsoft.com.br"
                )
        )
)
public class OpenApiConfig {
}