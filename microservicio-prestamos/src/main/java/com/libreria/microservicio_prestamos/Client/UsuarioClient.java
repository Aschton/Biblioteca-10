package com.libreria.microservicio_prestamos.Client;

import com.libreria.microservicio_prestamos.DTO.UsuarioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Cliente Feign para hablar con microservicio-usuarios.
// La url se lee desde application.yml (usuarios.url).
@FeignClient(name = "microservicio-usuarios", url = "${usuarios.url}")
public interface UsuarioClient {

    // Llama a GET {usuarios.url}/api/usuarios/{id}
    @GetMapping("/api/usuarios/{id}")
    UsuarioDTO obtenerUsuarioPorId(@PathVariable("id") Long id);
}
