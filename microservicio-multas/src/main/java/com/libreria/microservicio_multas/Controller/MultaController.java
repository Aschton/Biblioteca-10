package com.libreria.microservicio_multas.Controller;

import com.libreria.microservicio_multas.DTO.MultaDTO;
import com.libreria.microservicio_multas.Model.Multa;
import com.libreria.microservicio_multas.Service.MultaService;
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
@RequestMapping("/api/multas")
@Tag(name = "Multas", description = "Gestion de multas por atraso. Valida prestamo y usuario via Feign")
public class MultaController {

    private static final Logger logger = LoggerFactory.getLogger(MultaController.class);

    @Autowired
    private MultaService multaService;

    @GetMapping
    @Operation(summary = "Listar multas", description = "Devuelve todas las multas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de multas obtenida correctamente")
    public ResponseEntity<List<Multa>> listar() {
        logger.info("[MultaController] GET /api/multas");
        return ResponseEntity.ok(multaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener multa por id", description = "Busca una multa segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Multa encontrada"),
            @ApiResponse(responseCode = "404", description = "Multa no encontrada")
    })
    public ResponseEntity<Multa> obtenerPorId(@PathVariable Long id) {
        logger.info("[MultaController] GET /api/multas/{}", id);
        return ResponseEntity.ok(multaService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear multa",
            description = "Crea una multa. Verifica que el prestamo y el usuario existan (Feign)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Multa creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Prestamo o usuario inexistente")
    })
    public ResponseEntity<Multa> crear(@Valid @RequestBody MultaDTO dto) {
        logger.info("[MultaController] POST /api/multas");
        return ResponseEntity.status(HttpStatus.CREATED).body(multaService.crear(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar multa", description = "Actualiza una multa existente segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Multa actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Multa no encontrada")
    })
    public ResponseEntity<Multa> actualizar(@PathVariable Long id, @Valid @RequestBody MultaDTO dto) {
        logger.info("[MultaController] PUT /api/multas/{}", id);
        return ResponseEntity.ok(multaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar multa", description = "Elimina una multa segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Multa eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Multa no encontrada")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("[MultaController] DELETE /api/multas/{}", id);
        multaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
