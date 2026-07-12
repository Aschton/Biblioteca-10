package com.libreria.microservicio_prestamos.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PrestamoDTO {

    @NotNull(message = "El id del libro no puede ser nulo")
    private Long libroId;

    @NotNull(message = "El id del usuario no puede ser nulo")
    private Long usuarioId;

    @NotBlank(message = "El estado no puede estar vacío")
    @Pattern(regexp = "ACTIVO|DEVUELTO", message = "El estado debe ser ACTIVO o DEVUELTO")
    private String estadoPrestamo;
}
