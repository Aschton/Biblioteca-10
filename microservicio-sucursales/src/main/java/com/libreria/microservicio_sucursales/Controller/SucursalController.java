package com.libreria.microservicio_sucursales.Controller;

import com.libreria.microservicio_sucursales.DTO.SucursalDTO;
import com.libreria.microservicio_sucursales.Model.Sucursal;
import com.libreria.microservicio_sucursales.Service.SucursalService;
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
@RequestMapping("/api/sucursales")
@Tag(name = "Sucursales", description = "Operaciones CRUD sobre sucursales")
public class SucursalController {

    private static final Logger logger = LoggerFactory.getLogger(SucursalController.class);

    @Autowired
    private SucursalService sucursalService;

    @GetMapping
    @Operation(summary = "Listar sucursales", description = "Devuelve todas las sucursales registradas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    public ResponseEntity<List<Sucursal>> listar() {
        logger.info("[SucursalController] GET /api/sucursales");
        return ResponseEntity.ok(sucursalService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sucursal por id", description = "Busca una sucursal segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucursal encontrada"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<Sucursal> obtenerPorId(@PathVariable Long id) {
        logger.info("[SucursalController] GET /api/sucursales/{}", id);
        return ResponseEntity.ok(sucursalService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear sucursal", description = "Crea una nueva sucursal")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sucursal creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    public ResponseEntity<Sucursal> crear(@Valid @RequestBody SucursalDTO dto) {
        logger.info("[SucursalController] POST /api/sucursales");
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalService.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sucursal", description = "Actualiza una sucursal existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucursal actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<Sucursal> actualizar(@PathVariable Long id, @Valid @RequestBody SucursalDTO dto) {
        logger.info("[SucursalController] PUT /api/sucursales/{}", id);
        return ResponseEntity.ok(sucursalService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar sucursal", description = "Elimina una sucursal segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sucursal eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Sucursal no encontrada")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("[SucursalController] DELETE /api/sucursales/{}", id);
        sucursalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
