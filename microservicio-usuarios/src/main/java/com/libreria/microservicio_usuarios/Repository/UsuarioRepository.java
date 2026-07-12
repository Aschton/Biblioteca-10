package com.libreria.microservicio_usuarios.Repository;

import com.libreria.microservicio_usuarios.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository JPA real: Spring Data genera la implementacion en tiempo de
// ejecucion. findAll, findById, save, existsById y deleteById ya vienen
// incluidos en JpaRepository, por eso el Service no cambia.
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByCorreo(String correo);
}
