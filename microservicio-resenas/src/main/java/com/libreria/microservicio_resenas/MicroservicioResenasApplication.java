package com.libreria.microservicio_resenas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroservicioResenasApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroservicioResenasApplication.class, args);
    }
}
