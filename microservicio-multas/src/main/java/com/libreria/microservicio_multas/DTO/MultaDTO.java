package com.libreria.microservicio_multas.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MultaDTO {

    @NotNull(message = "El id del prestamo no puede ser nulo")
    private Long prestamoId;

    @NotNull(message = "El id del usuario no puede ser nulo")
    private Long usuarioId;

    @NotNull(message = "El monto no puede ser nulo")
    @Positive(message = "El monto debe ser mayor a cero")
    private Double monto;

    @NotBlank(message = "El estado no puede estar vacio")
    @Pattern(regexp = "PENDIENTE|PAGADA", message = "El estado debe ser PENDIENTE o PAGADA")
    private String estadoMulta;
}
