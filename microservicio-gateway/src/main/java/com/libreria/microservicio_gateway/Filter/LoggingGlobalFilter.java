package com.libreria.microservicio_gateway.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

// Filtro GLOBAL: se ejecuta en TODAS las solicitudes que pasan por el Gateway.
// Aqui solo registramos en consola que ruta entro y a donde se reenvia.
// Sirve para demostrar el uso de "filtros" en el Gateway (rubrica 3.3.2 / 3.3.3).
@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        logger.info("[Gateway] Entrada -> metodo={} ruta={}", request.getMethod(), request.getURI().getPath());

        // chain.filter(...) continua con el siguiente filtro / reenvio al microservicio.
        // Cuando termina la respuesta, registramos el codigo de estado de salida.
        return chain.filter(exchange).then(Mono.fromRunnable(() ->
                logger.info("[Gateway] Salida  -> ruta={} status={}",
                        request.getURI().getPath(),
                        exchange.getResponse().getStatusCode())
        ));
    }

    // Orden de ejecucion del filtro. Un numero bajo = se ejecuta primero.
    @Override
    public int getOrder() {
        return -1;
    }
}
