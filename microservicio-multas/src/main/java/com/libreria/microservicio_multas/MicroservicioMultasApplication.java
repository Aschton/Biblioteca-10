package com.libreria.microservicio_multas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroservicioMultasApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroservicioMultasApplication.class, args);
    }
}
