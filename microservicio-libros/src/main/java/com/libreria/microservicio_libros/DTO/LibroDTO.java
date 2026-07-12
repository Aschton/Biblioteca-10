package com.libreria.microservicio_libros.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

// DTO: solo recibe los datos del usuario, sin incluir el id
@Data
public class LibroDTO {

    @NotBlank(message = "El titulo no puede estar vacio")
    private String titulo;

    @NotBlank(message = "El autor no puede estar vacio")
    private String autor;

    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    @NotBlank(message = "El estado no puede estar vacio")
    @Pattern(regexp = "nuevo|usado", message = "El estado debe ser nuevo o usado")
    private String estado;
}
