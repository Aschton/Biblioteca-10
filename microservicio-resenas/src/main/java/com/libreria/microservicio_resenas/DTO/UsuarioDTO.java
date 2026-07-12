package com.libreria.microservicio_resenas.DTO;

import lombok.Data;

// Representa la respuesta de GET /api/usuarios/{id} del microservicio-usuarios.
@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String rut;
    private String correo;
}
