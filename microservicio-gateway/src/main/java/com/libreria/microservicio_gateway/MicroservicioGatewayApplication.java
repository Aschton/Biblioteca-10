package com.libreria.microservicio_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// API Gateway de la librería.
// Centraliza todas las solicitudes y las reenvía al microservicio que corresponda
// segun la ruta. NO usa Eureka: las direcciones de los microservicios estan
// definidas de forma fija (estatica) en application.yml.
@SpringBootApplication
public class MicroservicioGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroservicioGatewayApplication.class, args);
    }
}
