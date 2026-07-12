package com.libreria.microservicio_reservas.DTO;

import lombok.Data;

// Representa la respuesta de GET /api/libros/{id} del microservicio-libros.
@Data
public class LibroDTO {
    private Long id;
    private String titulo;
    private String autor;
    private Integer stock;
    private String estado;
}
