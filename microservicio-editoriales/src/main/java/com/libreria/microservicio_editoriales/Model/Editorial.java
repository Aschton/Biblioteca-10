package com.libreria.microservicio_editoriales.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entidad JPA real: Hibernate gestiona la tabla 'editoriales' automaticamente
// (spring.jpa.hibernate.ddl-auto=update) a partir de estas anotaciones.
@Entity
@Table(name = "editoriales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Editorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String pais;
}
