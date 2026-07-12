package com.libreria.microservicio_usuarios.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configuracion de la documentacion Swagger / OpenAPI para este microservicio.
// Define el titulo, version y descripcion que se ven arriba en la UI de Swagger.
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI usuariosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Usuarios - API")
                        .version("1.0")
                        .description("Gestiona los usuarios registrados en la libreria (CRUD)."));
    }
}
