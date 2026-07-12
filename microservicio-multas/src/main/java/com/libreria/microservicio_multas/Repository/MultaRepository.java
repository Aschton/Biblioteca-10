package com.libreria.microservicio_multas.Repository;

import com.libreria.microservicio_multas.Model.Multa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository JPA real: Spring Data genera la implementacion en tiempo de
// ejecucion. findAll, findById, save, existsById y deleteById ya vienen
// incluidos en JpaRepository, por eso el Service no cambia.
@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {
}
