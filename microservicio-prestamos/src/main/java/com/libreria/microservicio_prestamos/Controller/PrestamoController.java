package com.libreria.microservicio_prestamos.Controller;

import com.libreria.microservicio_prestamos.DTO.PrestamoDTO;
import com.libreria.microservicio_prestamos.Model.Prestamo;
import com.libreria.microservicio_prestamos.Service.PrestamoService;
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

// Se elimino la anotacion @Controller que estaba de mas: con @RestController basta.
@RestController
@RequestMapping("/api/prestamos")
@Tag(name = "Prestamos", description = "Gestion de prestamos. Valida libro y usuario via Feign")
public class PrestamoController {

    private static final Logger logger = LoggerFactory.getLogger(PrestamoController.class);

    @Autowired
    private PrestamoService prestamoService;

    // GET /api/prestamos
    @GetMapping
    @Operation(summary = "Listar prestamos", description = "Devuelve todos los prestamos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de prestamos obtenida correctamente")
    public ResponseEntity<List<Prestamo>> listar() {
        logger.info("[PrestamoController] GET /api/prestamos");
        return ResponseEntity.ok(prestamoService.obtenerTodos());
    }

    // GET /api/prestamos/1
    @GetMapping("/{id}")
    @Operation(summary = "Obtener prestamo por id", description = "Busca un prestamo segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prestamo encontrado"),
            @ApiResponse(responseCode = "404", description = "Prestamo no encontrado")
    })
    public ResponseEntity<Prestamo> obtenerPorId(@PathVariable Long id) {
        logger.info("[PrestamoController] GET /api/prestamos/{}", id);
        return ResponseEntity.ok(prestamoService.obtenerPorId(id));
    }

    // POST /api/prestamos
    @PostMapping
    @Operation(summary = "Crear prestamo",
            description = "Crea un prestamo. Verifica que el libro y el usuario existan (Feign) y que haya stock")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prestamo creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Libro o usuario inexistente, o sin stock")
    })
    public ResponseEntity<Prestamo> crear(@Valid @RequestBody PrestamoDTO dto) {
        logger.info("[PrestamoController] POST /api/prestamos");
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoService.crear(dto));
    }

    // PUT /api/prestamos/1
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar prestamo", description = "Actualiza un prestamo existente segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prestamo actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Prestamo no encontrado")
    })
    public ResponseEntity<Prestamo> actualizar(@PathVariable Long id, @Valid @RequestBody PrestamoDTO dto) {
        logger.info("[PrestamoController] PUT /api/prestamos/{}", id);
        return ResponseEntity.ok(prestamoService.actualizar(id, dto));
    }

    // DELETE /api/prestamos/1
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar prestamo", description = "Elimina un prestamo segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Prestamo eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Prestamo no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("[PrestamoController] DELETE /api/prestamos/{}", id);
        prestamoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
