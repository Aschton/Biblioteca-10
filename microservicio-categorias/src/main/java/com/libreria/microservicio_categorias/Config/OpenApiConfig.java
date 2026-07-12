package com.libreria.microservicio_categorias.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI categoriasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Categorias - API")
                        .version("1.0")
                        .description("Gestiona las categorias/generos de los libros (CRUD)."));
    }
}
