package com.libreria.microservicio_libros.Controller;

import com.libreria.microservicio_libros.DTO.LibroDTO;
import com.libreria.microservicio_libros.Model.Libro;
import com.libreria.microservicio_libros.Service.LibroService;
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
@RequestMapping("/api/libros")
@Tag(name = "Libros", description = "Operaciones CRUD sobre el catalogo de libros")
public class LibroController {

    // Logger para ver las peticiones que llegan
    private static final Logger logger = LoggerFactory.getLogger(LibroController.class);

    @Autowired
    private LibroService libroService;

    // GET /api/libros - retorna todos los libros
    @GetMapping
    @Operation(summary = "Listar libros", description = "Devuelve todos los libros registrados")
    @ApiResponse(responseCode = "200", description = "Lista de libros obtenida correctamente")
    public ResponseEntity<List<Libro>> listar() {
        logger.info("[LibroController] GET /api/libros");
        return ResponseEntity.ok(libroService.obtenerTodos());
    }

    // GET /api/libros/1 - retorna un libro por id
    @GetMapping("/{id}")
    @Operation(summary = "Obtener libro por id", description = "Busca un libro segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        logger.info("[LibroController] GET /api/libros/{}", id);
        return ResponseEntity.ok(libroService.obtenerPorId(id));
    }

    // POST /api/libros - crea un nuevo libro
    // @Valid activa las validaciones del DTO
    @PostMapping
    @Operation(summary = "Crear libro", description = "Crea un nuevo libro a partir de los datos enviados")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Libro creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos (validacion fallida)")
    })
    public ResponseEntity<Libro> crear(@Valid @RequestBody LibroDTO dto) {
        logger.info("[LibroController] POST /api/libros");
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.guardar(dto));
    }

    // PUT /api/libros/1 - actualiza un libro existente
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar libro", description = "Actualiza un libro existente segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @Valid @RequestBody LibroDTO dto) {
        logger.info("[LibroController] PUT /api/libros/{}", id);
        return ResponseEntity.ok(libroService.actualizar(id, dto));
    }

    // DELETE /api/libros/1 - elimina un libro
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar libro", description = "Elimina un libro segun su id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Libro eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("[LibroController] DELETE /api/libros/{}", id);
        libroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
