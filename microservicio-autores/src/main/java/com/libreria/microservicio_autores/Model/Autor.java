package com.libreria.microservicio_autores.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entidad JPA real: Hibernate gestiona la tabla 'autores' automaticamente
// (spring.jpa.hibernate.ddl-auto=update) a partir de estas anotaciones.
@Entity
@Table(name = "autores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String nacionalidad;
    private String biografia;
}
