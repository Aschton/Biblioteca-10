package com.libreria.microservicio_editoriales.Service;

import com.libreria.microservicio_editoriales.DTO.EditorialDTO;
import com.libreria.microservicio_editoriales.Model.Editorial;
import com.libreria.microservicio_editoriales.Repository.EditorialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditorialService {

    private static final Logger logger = LoggerFactory.getLogger(EditorialService.class);

    @Autowired
    private EditorialRepository editorialRepository;

    public List<Editorial> obtenerTodos() {
        logger.info("[EditorialService] Obteniendo todas las editoriales");
        return editorialRepository.findAll();
    }

    public Editorial obtenerPorId(Long id) {
        logger.info("[EditorialService] Buscando editorial con id={}", id);
        return editorialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Editorial no encontrada con id: " + id));
    }

    public Editorial guardar(EditorialDTO dto) {
        logger.info("[EditorialService] Creando editorial: {}", dto.getNombre());
        Editorial editorial = new Editorial();
        editorial.setNombre(dto.getNombre());
        editorial.setPais(dto.getPais());
        Editorial guardada = editorialRepository.save(editorial);
        logger.info("[EditorialService] Editorial creada con id={}", guardada.getId());
        return guardada;
    }

    public Editorial actualizar(Long id, EditorialDTO dto) {
        logger.info("[EditorialService] Actualizando editorial con id={}", id);
        Editorial editorial = editorialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Editorial no encontrada con id: " + id));
        editorial.setNombre(dto.getNombre());
        editorial.setPais(dto.getPais());
        Editorial actualizada = editorialRepository.save(editorial);
        logger.info("[EditorialService] Editorial id={} actualizada", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        logger.info("[EditorialService] Eliminando editorial con id={}", id);
        if (!editorialRepository.existsById(id)) {
            throw new RuntimeException("Editorial no encontrada con id: " + id);
        }
        editorialRepository.deleteById(id);
        logger.info("[EditorialService] Editorial id={} eliminada", id);
    }
}
