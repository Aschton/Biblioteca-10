package com.libreria.microservicio_autores.Service;

import com.libreria.microservicio_autores.DTO.AutorDTO;
import com.libreria.microservicio_autores.Model.Autor;
import com.libreria.microservicio_autores.Repository.AutorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {

    private static final Logger logger = LoggerFactory.getLogger(AutorService.class);

    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> obtenerTodos() {
        logger.info("[AutorService] Obteniendo todos los autores");
        return autorRepository.findAll();
    }

    public Autor obtenerPorId(Long id) {
        logger.info("[AutorService] Buscando autor con id={}", id);
        return autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado con id: " + id));
    }

    public Autor guardar(AutorDTO dto) {
        logger.info("[AutorService] Creando autor: {}", dto.getNombre());
        Autor autor = new Autor();
        autor.setNombre(dto.getNombre());
        autor.setNacionalidad(dto.getNacionalidad());
        autor.setBiografia(dto.getBiografia());
        Autor guardado = autorRepository.save(autor);
        logger.info("[AutorService] Autor creado con id={}", guardado.getId());
        return guardado;
    }

    public Autor actualizar(Long id, AutorDTO dto) {
        logger.info("[AutorService] Actualizando autor con id={}", id);
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado con id: " + id));
        autor.setNombre(dto.getNombre());
        autor.setNacionalidad(dto.getNacionalidad());
        autor.setBiografia(dto.getBiografia());
        Autor actualizado = autorRepository.save(autor);
        logger.info("[AutorService] Autor id={} actualizado", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        logger.info("[AutorService] Eliminando autor con id={}", id);
        if (!autorRepository.existsById(id)) {
            throw new RuntimeException("Autor no encontrado con id: " + id);
        }
        autorRepository.deleteById(id);
        logger.info("[AutorService] Autor id={} eliminado", id);
    }
}
