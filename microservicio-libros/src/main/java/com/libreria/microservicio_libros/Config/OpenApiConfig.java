package com.libreria.microservicio_libros.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configuracion de la documentacion Swagger / OpenAPI para este microservicio.
// Define el titulo, version y descripcion que se ven arriba en la UI de Swagger.
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI librosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Libros - API")
                        .version("1.0")
                        .description("Gestiona el catalogo de libros de la libreria (CRUD)."));
    }
}
