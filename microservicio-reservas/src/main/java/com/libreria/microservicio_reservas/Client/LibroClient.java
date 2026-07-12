package com.libreria.microservicio_reservas.Client;

import com.libreria.microservicio_reservas.DTO.LibroDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-libros", url = "${libros.url}")
public interface LibroClient {

    @GetMapping("/api/libros/{id}")
    LibroDTO obtenerLibroPorId(@PathVariable("id") Long id);
}
