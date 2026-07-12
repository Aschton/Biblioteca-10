package com.libreria.microservicio_autores.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AutorDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "La nacionalidad no puede estar vacia")
    private String nacionalidad;

    @Size(max = 2000, message = "La biografia no puede superar los 2000 caracteres")
    private String biografia;
}
