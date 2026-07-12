package com.libreria.microservicio_multas.Client;

import com.libreria.microservicio_multas.DTO.PrestamoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-prestamos", url = "${prestamos.url}")
public interface PrestamoClient {

    @GetMapping("/api/prestamos/{id}")
    PrestamoDTO obtenerPrestamoPorId(@PathVariable("id") Long id);
}
