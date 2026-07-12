package com.libreria.microservicio_sucursales.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entidad JPA real: Hibernate gestiona la tabla 'sucursales' automaticamente
// (spring.jpa.hibernate.ddl-auto=update) a partir de estas anotaciones.
@Entity
@Table(name = "sucursales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;
    private String telefono;
}
