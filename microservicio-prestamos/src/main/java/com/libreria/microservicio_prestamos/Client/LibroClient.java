package com.libreria.microservicio_prestamos.Client;

import com.libreria.microservicio_prestamos.DTO.LibroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// Cliente Feign para hablar con microservicio-libros.
// La url se lee desde application.properties (libros.url). Asi no queda "quemada" en el codigo
// y se puede cambiar sin recompilar.
@FeignClient(name = "microservicio-libros", url = "${libros.url}")
public interface LibroClient {

    // Llama a GET {libros.url}/api/libros/{id}
    @GetMapping("/api/libros/{id}")
    LibroDTO obtenerLibroPorId(@PathVariable("id") Long id);

    // Llama a PUT {libros.url}/api/libros/{id}
    // Se usa para descontar/reponer stock cuando un prestamo cambia de estado.
    @PutMapping("/api/libros/{id}")
    LibroDTO actualizarLibro(@PathVariable("id") Long id, @RequestBody LibroDTO dto);
}
