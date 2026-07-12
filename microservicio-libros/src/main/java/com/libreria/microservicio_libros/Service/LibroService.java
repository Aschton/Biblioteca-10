package com.libreria.microservicio_libros.Service;

import com.libreria.microservicio_libros.DTO.LibroDTO;
import com.libreria.microservicio_libros.Model.Libro;
import com.libreria.microservicio_libros.Repository.LibroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {

    // Logger para registrar lo que pasa en el servicio
    private static final Logger logger = LoggerFactory.getLogger(LibroService.class);

    @Autowired
    private LibroRepository libroRepository;

    // Retorna todos los libros de la base de datos
    public List<Libro> obtenerTodos() {
        logger.info("[LibroService] Obteniendo todos los libros");
        return libroRepository.findAll();
    }

    // Busca un libro por id, si no existe lanza un error
    public Libro obtenerPorId(Long id) {
        logger.info("[LibroService] Buscando libro con id={}", id);
        return libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
    }

    // Recibe un DTO, crea el objeto Libro y lo guarda
    public Libro guardar(LibroDTO dto) {
        logger.info("[LibroService] Creando libro: {}", dto.getTitulo());
        Libro libro = new Libro();
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setStock(dto.getStock());
        libro.setEstado(dto.getEstado());
        Libro guardado = libroRepository.save(libro);
        logger.info("[LibroService] Libro creado con id={}", guardado.getId());
        return guardado;
    }

    // Busca el libro, actualiza sus campos y lo guarda
    public Libro actualizar(Long id, LibroDTO dto) {
        logger.info("[LibroService] Actualizando libro con id={}", id);
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setStock(dto.getStock());
        libro.setEstado(dto.getEstado());
        Libro actualizado = libroRepository.save(libro);
        logger.info("[LibroService] Libro id={} actualizado", id);
        return actualizado;
    }

    // Elimina el libro si existe
    public void eliminar(Long id) {
        logger.info("[LibroService] Eliminando libro con id={}", id);
        if (!libroRepository.existsById(id)) {
            throw new RuntimeException("Libro no encontrado con id: " + id);
        }
        libroRepository.deleteById(id);
        logger.info("[LibroService] Libro id={} eliminado", id);
    }
}
