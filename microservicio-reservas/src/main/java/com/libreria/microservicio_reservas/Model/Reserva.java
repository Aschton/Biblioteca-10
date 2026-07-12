package com.libreria.microservicio_reservas.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

// Entidad JPA real: Hibernate gestiona la tabla 'reservas' automaticamente
// (spring.jpa.hibernate.ddl-auto=update) a partir de estas anotaciones.
@Entity
@Table(name = "reservas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long libroId;
    private Long usuarioId;
    private LocalDate fechaReserva;
    private String estadoReserva;
}
