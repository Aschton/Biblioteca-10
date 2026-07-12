package com.libreria.microservicio_resenas.Controller;

import com.libreria.microservicio_resenas.DTO.ResenaDTO;
import com.libreria.microservicio_resenas.Model.Resena;
import com.libreria.microservicio_resenas.Service.ResenaService;
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
@RequestMapping("/api/resenas")
@Tag(name = "Resenas", description = "Gestion de resenas de libros. Valida usuario y libro via Feign")
public class ResenaController {

    private static final Logger logger = LoggerFactory.getLogger(ResenaController.class);

    @Autowired
    private ResenaService resenaService;

    @GetMapping
    @Operation(summary = "Listar resenas", description = "Devuelve todas las resenas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de resenas obtenida correctamente")
    public ResponseEntity<List<Resena>> listar() {
        logger.info("[ResenaController] GET /api/resenas");
        return ResponseEntity.ok(resenaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener resena por id", description = "Busca una resena segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resena encontrada"),
            @ApiResponse(responseCode = "404", description = "Resena no encontrada")
    })
    public ResponseEntity<Resena> obtenerPorId(@PathVariable Long id) {
        logger.info("[ResenaController] GET /api/resenas/{}", id);
        return ResponseEntity.ok(resenaService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear resena",
            description = "Crea una resena. Verifica que el usuario y el libro existan (Feign)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Resena creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Usuario o libro inexistente")
    })
    public ResponseEntity<Resena> crear(@Valid @RequestBody ResenaDTO dto) {
        logger.info("[ResenaController] POST /api/resenas");
        return ResponseEntity.status(HttpStatus.CREATED).body(resenaService.crear(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar resena", description = "Actualiza una resena existente segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resena actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Resena no encontrada")
    })
    public ResponseEntity<Resena> actualizar(@PathVariable Long id, @Valid @RequestBody ResenaDTO dto) {
        logger.info("[ResenaController] PUT /api/resenas/{}", id);
        return ResponseEntity.ok(resenaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar resena", description = "Elimina una resena segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Resena eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Resena no encontrada")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("[ResenaController] DELETE /api/resenas/{}", id);
        resenaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
