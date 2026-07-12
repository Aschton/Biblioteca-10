package com.libreria.microservicio_categorias.Service;

import com.libreria.microservicio_categorias.DTO.CategoriaDTO;
import com.libreria.microservicio_categorias.Model.Categoria;
import com.libreria.microservicio_categorias.Repository.CategoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaService.class);

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> obtenerTodos() {
        logger.info("[CategoriaService] Obteniendo todas las categorias");
        return categoriaRepository.findAll();
    }

    public Categoria obtenerPorId(Long id) {
        logger.info("[CategoriaService] Buscando categoria con id={}", id);
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + id));
    }

    public Categoria guardar(CategoriaDTO dto) {
        logger.info("[CategoriaService] Creando categoria: {}", dto.getNombre());
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        Categoria guardada = categoriaRepository.save(categoria);
        logger.info("[CategoriaService] Categoria creada con id={}", guardada.getId());
        return guardada;
    }

    public Categoria actualizar(Long id, CategoriaDTO dto) {
        logger.info("[CategoriaService] Actualizando categoria con id={}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + id));
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        Categoria actualizada = categoriaRepository.save(categoria);
        logger.info("[CategoriaService] Categoria id={} actualizada", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        logger.info("[CategoriaService] Eliminando categoria con id={}", id);
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoria no encontrada con id: " + id);
        }
        categoriaRepository.deleteById(id);
        logger.info("[CategoriaService] Categoria id={} eliminada", id);
    }
}
