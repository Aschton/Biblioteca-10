package com.libreria.microservicio_resenas.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI resenasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Resenas - API")
                        .version("1.0")
                        .description("Gestiona resenas de usuarios sobre libros. Valida usuario y libro via Feign."));
    }
}
