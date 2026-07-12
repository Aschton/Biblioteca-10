package com.libreria.microservicio_multas.DTO;

import lombok.Data;

// Representa la respuesta de GET /api/prestamos/{id} del microservicio-prestamos.
@Data
public class PrestamoDTO {
    private Long id;
    private Long libroId;
    private Long usuarioId;
    private String fechaPrestamo;
    private String estadoPrestamo;
}
