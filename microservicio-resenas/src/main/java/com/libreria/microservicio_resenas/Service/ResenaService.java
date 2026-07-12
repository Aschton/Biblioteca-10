package com.libreria.microservicio_resenas.Service;

import com.libreria.microservicio_resenas.Client.LibroClient;
import com.libreria.microservicio_resenas.Client.UsuarioClient;
import com.libreria.microservicio_resenas.DTO.LibroDTO;
import com.libreria.microservicio_resenas.DTO.ResenaDTO;
import com.libreria.microservicio_resenas.DTO.UsuarioDTO;
import com.libreria.microservicio_resenas.Model.Resena;
import com.libreria.microservicio_resenas.Repository.ResenaRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ResenaService {

    private static final Logger logger = LoggerFactory.getLogger(ResenaService.class);

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private UsuarioClient usuarioClient;

    @Autowired
    private LibroClient libroClient;

    public List<Resena> obtenerTodos() {
        logger.info("[ResenaService] Obteniendo todas las resenas");
        return resenaRepository.findAll();
    }

    public Resena obtenerPorId(Long id) {
        logger.info("[ResenaService] Buscando resena con id={}", id);
        return resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resena no encontrada con id: " + id));
    }

    public Resena crear(ResenaDTO dto) {
        logger.info("[ResenaService] Creando resena para usuarioId={} y libroId={}",
                dto.getUsuarioId(), dto.getLibroId());

        // 1) Verificar que el usuario existe (Feign).
        UsuarioDTO usuario = obtenerUsuarioRemoto(dto.getUsuarioId());
        logger.info("[ResenaService] Usuario encontrado: {}", usuario.getNombre());

        // 2) Verificar que el libro existe (Feign).
        LibroDTO libro = obtenerLibroRemoto(dto.getLibroId());
        logger.info("[ResenaService] Libro encontrado: {}", libro.getTitulo());

        Resena resena = new Resena();
        resena.setUsuarioId(dto.getUsuarioId());
        resena.setLibroId(dto.getLibroId());
        resena.setPuntuacion(dto.getPuntuacion());
        resena.setComentario(dto.getComentario());
        resena.setFecha(LocalDate.now());

        Resena guardada = resenaRepository.save(resena);
        logger.info("[ResenaService] Resena creada con id={}", guardada.getId());
        return guardada;
    }

    public Resena actualizar(Long id, ResenaDTO dto) {
        logger.info("[ResenaService] Actualizando resena con id={}", id);
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resena no encontrada con id: " + id));
        resena.setPuntuacion(dto.getPuntuacion());
        resena.setComentario(dto.getComentario());
        Resena actualizada = resenaRepository.save(resena);
        logger.info("[ResenaService] Resena id={} actualizada", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        logger.info("[ResenaService] Eliminando resena con id={}", id);
        if (!resenaRepository.existsById(id)) {
            throw new RuntimeException("Resena no encontrada con id: " + id);
        }
        resenaRepository.deleteById(id);
        logger.info("[ResenaService] Resena id={} eliminada", id);
    }

    private UsuarioDTO obtenerUsuarioRemoto(Long usuarioId) {
        try {
            return usuarioClient.obtenerUsuarioPorId(usuarioId);
        } catch (FeignException.NotFound e) {
            logger.warn("[ResenaService] El usuario con id={} no existe", usuarioId);
            throw new RuntimeException("El usuario con id " + usuarioId + " no existe");
        } catch (FeignException e) {
            logger.error("[ResenaService] Error al consultar microservicio-usuarios: {}", e.getMessage());
            throw new RuntimeException("No se pudo contactar al microservicio de usuarios");
        }
    }

    private LibroDTO obtenerLibroRemoto(Long libroId) {
        try {
            return libroClient.obtenerLibroPorId(libroId);
        } catch (FeignException.NotFound e) {
            logger.warn("[ResenaService] El libro con id={} no existe", libroId);
            throw new RuntimeException("El libro con id " + libroId + " no existe");
        } catch (FeignException e) {
            logger.error("[ResenaService] Error al consultar microservicio-libros: {}", e.getMessage());
            throw new RuntimeException("No se pudo contactar al microservicio de libros");
        }
    }
}
