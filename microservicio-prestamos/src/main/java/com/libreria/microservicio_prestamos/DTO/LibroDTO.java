package com.libreria.microservicio_prestamos.DTO;

import lombok.Data;

// Este DTO representa los datos que nos devuelve microservicio-libros
// Debe tener los mismos campos que devuelve ese microservicio
@Data
public class LibroDTO {
    private Long id;
    private String titulo;
    private String autor;
    private Integer stock;
    private String estado;
}
