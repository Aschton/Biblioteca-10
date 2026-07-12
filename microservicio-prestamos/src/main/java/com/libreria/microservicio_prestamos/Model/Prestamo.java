package com.libreria.microservicio_prestamos.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

// Entidad JPA real: Hibernate gestiona la tabla 'prestamos' automaticamente
// (spring.jpa.hibernate.ddl-auto=update) a partir de estas anotaciones.
@Entity
@Table(name = "prestamos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long libroId;
    private Long usuarioId;
    private LocalDate fechaPrestamo;
    private String estadoPrestamo;
}
