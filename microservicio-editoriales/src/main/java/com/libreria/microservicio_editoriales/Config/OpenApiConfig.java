package com.libreria.microservicio_editoriales.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI editorialesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Editoriales - API")
                        .version("1.0")
                        .description("Gestiona las editoriales de los libros (CRUD)."));
    }
}
