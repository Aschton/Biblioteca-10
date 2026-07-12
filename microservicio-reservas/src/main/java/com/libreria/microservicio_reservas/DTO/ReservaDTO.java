package com.libreria.microservicio_reservas.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ReservaDTO {

    @NotNull(message = "El id del libro no puede ser nulo")
    private Long libroId;

    @NotNull(message = "El id del usuario no puede ser nulo")
    private Long usuarioId;

    @NotBlank(message = "El estado no puede estar vacio")
    @Pattern(regexp = "PENDIENTE|CONFIRMADA|CANCELADA", message = "El estado debe ser PENDIENTE, CONFIRMADA o CANCELADA")
    private String estadoReserva;
}
