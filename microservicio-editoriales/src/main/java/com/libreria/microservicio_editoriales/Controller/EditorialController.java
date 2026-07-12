package com.libreria.microservicio_editoriales.Controller;

import com.libreria.microservicio_editoriales.DTO.EditorialDTO;
import com.libreria.microservicio_editoriales.Model.Editorial;
import com.libreria.microservicio_editoriales.Service.EditorialService;
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
@RequestMapping("/api/editoriales")
@Tag(name = "Editoriales", description = "Operaciones CRUD sobre editoriales")
public class EditorialController {

    private static final Logger logger = LoggerFactory.getLogger(EditorialController.class);

    @Autowired
    private EditorialService editorialService;

    @GetMapping
    @Operation(summary = "Listar editoriales", description = "Devuelve todas las editoriales registradas")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    public ResponseEntity<List<Editorial>> listar() {
        logger.info("[EditorialController] GET /api/editoriales");
        return ResponseEntity.ok(editorialService.obtenerTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener editorial por id", description = "Busca una editorial segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Editorial encontrada"),
            @ApiResponse(responseCode = "404", description = "Editorial no encontrada")
    })
    public ResponseEntity<Editorial> obtenerPorId(@PathVariable Long id) {
        logger.info("[EditorialController] GET /api/editoriales/{}", id);
        return ResponseEntity.ok(editorialService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear editorial", description = "Crea una nueva editorial")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Editorial creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    public ResponseEntity<Editorial> crear(@Valid @RequestBody EditorialDTO dto) {
        logger.info("[EditorialController] POST /api/editoriales");
        return ResponseEntity.status(HttpStatus.CREATED).body(editorialService.guardar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar editorial", description = "Actualiza una editorial existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Editorial actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Editorial no encontrada")
    })
    public ResponseEntity<Editorial> actualizar(@PathVariable Long id, @Valid @RequestBody EditorialDTO dto) {
        logger.info("[EditorialController] PUT /api/editoriales/{}", id);
        return ResponseEntity.ok(editorialService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar editorial", description = "Elimina una editorial segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Editorial eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Editorial no encontrada")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("[EditorialController] DELETE /api/editoriales/{}", id);
        editorialService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
