package com.libreria.microservicio_sucursales.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SucursalDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotBlank(message = "La direccion no puede estar vacia")
    private String direccion;

    private String telefono;
}
