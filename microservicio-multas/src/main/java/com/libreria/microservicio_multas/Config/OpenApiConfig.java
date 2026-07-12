package com.libreria.microservicio_multas.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI multasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Multas - API")
                        .version("1.0")
                        .description("Gestiona multas por atraso en prestamos. Valida prestamo y usuario via Feign."));
    }
}
