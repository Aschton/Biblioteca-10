package com.libreria.microservicio_reservas.Controller;

import com.libreria.microservicio_reservas.DTO.ReservaDTO;
import com.libreria.microservicio_reservas.Model.Reserva;
import com.libreria.microservicio_reservas.Service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@Tag(name = "Reservas", description = "Gestion de reservas de libros. Valida libro y usuario via Feign")
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    @Operation(summary = "Listar reservas", description = "Devuelve todas las reservas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida correctamente")
    public ResponseEntity<List<Reserva>> listar() {
        logger.info("[ReservaController] GET /api/reservas");
        return ResponseEntity.ok(reservaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por id", description = "Busca una reserva segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<Reserva> obtenerPorId(@PathVariable Long id) {
        logger.info("[ReservaController] GET /api/reservas/{}", id);
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear reserva",
            description = "Crea una reserva. Verifica que el libro y el usuario existan (Feign)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Libro o usuario inexistente")
    })
    public ResponseEntity<Reserva> crear(@Valid @RequestBody ReservaDTO dto) {
        logger.info("[ReservaController] POST /api/reservas");
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.crear(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reserva", description = "Actualiza una reserva existente segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<Reserva> actualizar(@PathVariable Long id, @Valid @RequestBody ReservaDTO dto) {
        logger.info("[ReservaController] PUT /api/reservas/{}", id);
        return ResponseEntity.ok(reservaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reserva", description = "Elimina una reserva segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("[ReservaController] DELETE /api/reservas/{}", id);
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
