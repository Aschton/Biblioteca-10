package com.libreria.microservicio_editoriales.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EditorialDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "El pais no puede estar vacio")
    private String pais;
}
