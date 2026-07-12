package com.libreria.microservicio_prestamos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

// @EnableFeignClients activa el uso de Feign en este microservicio
// Sin esto los @FeignClient no funcionan
@SpringBootApplication
@EnableFeignClients
public class MicroservicioPrestamosApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroservicioPrestamosApplication.class, args);
    }
}
