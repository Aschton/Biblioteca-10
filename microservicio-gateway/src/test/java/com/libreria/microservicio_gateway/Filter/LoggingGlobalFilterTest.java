package com.libreria.microservicio_gateway.Filter;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Pruebas unitarias del filtro global del Gateway.
// Se verifica que el filtro deja pasar la solicitud hacia la cadena (chain.filter)
// sin alterar la respuesta, y que efectivamente se ejecuta (Given-When-Then).
class LoggingGlobalFilterTest {

    private final LoggingGlobalFilter filtro = new LoggingGlobalFilter();

    @Test
    void filter_dejaPasarLaSolicitudHaciaLaCadena() {
        // Given: una solicitud simulada hacia /api/libros
        ServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/api/libros").build());
        exchange.getResponse().setStatusCode(HttpStatus.OK);

        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // When + Then: el filtro debe completar sin error y delegar a la cadena
        StepVerifier.create(filtro.filter(exchange, chain))
                .verifyComplete();

        verify(chain).filter(exchange);
    }

    @Test
    void getOrder_devuelveMenosUno() {
        // El filtro debe ejecutarse temprano (orden -1)
        assertEquals(-1, filtro.getOrder());
    }
}
