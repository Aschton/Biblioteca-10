package com.libreria.microservicio_autores.Controller;

import com.libreria.microservicio_autores.DTO.AutorDTO;
import com.libreria.microservicio_autores.Model.Autor;
import com.libreria.microservicio_autores.Service.AutorService;
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
@RequestMapping("/api/autores")
@Tag(name = "Autores", description = "Operaciones CRUD sobre los autores")
public class AutorController {

    private static final Logger logger = LoggerFactory.getLogger(AutorController.class);

    @Autowired
    private AutorService autorService;

    @GetMapping
    @Operation(summary = "Listar autores", description = "Devuelve todos los autores registrados")
    @ApiResponse(responseCode = "200", description = "Lista de autores obtenida correctamente")
    public ResponseEntity<List<Autor>> listar() {
        logger.info("[AutorController] GET /api/autores");
        return ResponseEntity.ok(autorService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener autor por id", description = "Busca un autor segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado"),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado")
    })
    public ResponseEntity<Autor> obtenerPorId(@PathVariable Long id) {
        logger.info("[AutorController] GET /api/autores/{}", id);
        return ResponseEntity.ok(autorService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear autor", description = "Crea un nuevo autor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Autor creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    public ResponseEntity<Autor> crear(@Valid @RequestBody AutorDTO dto) {
        logger.info("[AutorController] POST /api/autores");
        return ResponseEntity.status(HttpStatus.CREATED).body(autorService.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar autor", description = "Actualiza un autor existente segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado")
    })
    public ResponseEntity<Autor> actualizar(@PathVariable Long id, @Valid @RequestBody AutorDTO dto) {
        logger.info("[AutorController] PUT /api/autores/{}", id);
        return ResponseEntity.ok(autorService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar autor", description = "Elimina un autor segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Autor eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Autor no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("[AutorController] DELETE /api/autores/{}", id);
        autorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
