package com.libreria.microservicio_prestamos.DTO;

import lombok.Data;

// Este DTO representa los datos que nos devuelve microservicio-usuarios
// Debe tener los mismos campos que devuelve ese microservicio
@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String rut;
    private String correo;
}
