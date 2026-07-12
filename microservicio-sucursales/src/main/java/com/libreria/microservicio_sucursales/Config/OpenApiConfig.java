package com.libreria.microservicio_sucursales.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sucursalesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Sucursales - API")
                        .version("1.0")
                        .description("Gestiona las sucursales fisicas de la biblioteca (CRUD)."));
    }
}
