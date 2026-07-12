package com.libreria.microservicio_multas.Service;

import com.libreria.microservicio_multas.Client.PrestamoClient;
import com.libreria.microservicio_multas.Client.UsuarioClient;
import com.libreria.microservicio_multas.DTO.MultaDTO;
import com.libreria.microservicio_multas.DTO.PrestamoDTO;
import com.libreria.microservicio_multas.DTO.UsuarioDTO;
import com.libreria.microservicio_multas.Model.Multa;
import com.libreria.microservicio_multas.Repository.MultaRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MultaService {

    private static final Logger logger = LoggerFactory.getLogger(MultaService.class);

    @Autowired
    private MultaRepository multaRepository;

    @Autowired
    private PrestamoClient prestamoClient;

    @Autowired
    private UsuarioClient usuarioClient;

    public List<Multa> obtenerTodos() {
        logger.info("[MultaService] Obteniendo todas las multas");
        return multaRepository.findAll();
    }

    public Multa obtenerPorId(Long id) {
        logger.info("[MultaService] Buscando multa con id={}", id);
        return multaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multa no encontrada con id: " + id));
    }

    public Multa crear(MultaDTO dto) {
        logger.info("[MultaService] Creando multa para prestamoId={} y usuarioId={}",
                dto.getPrestamoId(), dto.getUsuarioId());

        // 1) Verificar que el prestamo existe (Feign).
        PrestamoDTO prestamo = obtenerPrestamoRemoto(dto.getPrestamoId());
        logger.info("[MultaService] Prestamo encontrado, estado={}", prestamo.getEstadoPrestamo());

        // 2) Verificar que el usuario existe (Feign).
        UsuarioDTO usuario = obtenerUsuarioRemoto(dto.getUsuarioId());
        logger.info("[MultaService] Usuario encontrado: {}", usuario.getNombre());

        // 3) Regla de negocio: la multa debe corresponder al mismo usuario que el prestamo.
        if (!prestamo.getUsuarioId().equals(dto.getUsuarioId())) {
            throw new RuntimeException("El usuario indicado no coincide con el usuario del prestamo");
        }

        Multa multa = new Multa();
        multa.setPrestamoId(dto.getPrestamoId());
        multa.setUsuarioId(dto.getUsuarioId());
        multa.setMonto(dto.getMonto());
        multa.setFechaGeneracion(LocalDate.now());
        multa.setEstadoMulta(dto.getEstadoMulta());

        Multa guardada = multaRepository.save(multa);
        logger.info("[MultaService] Multa creada con id={}", guardada.getId());
        return guardada;
    }

    public Multa actualizar(Long id, MultaDTO dto) {
        logger.info("[MultaService] Actualizando multa con id={}", id);
        Multa multa = multaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multa no encontrada con id: " + id));
        multa.setMonto(dto.getMonto());
        multa.setEstadoMulta(dto.getEstadoMulta());
        Multa actualizada = multaRepository.save(multa);
        logger.info("[MultaService] Multa id={} actualizada", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        logger.info("[MultaService] Eliminando multa con id={}", id);
        if (!multaRepository.existsById(id)) {
            throw new RuntimeException("Multa no encontrada con id: " + id);
        }
        multaRepository.deleteById(id);
        logger.info("[MultaService] Multa id={} eliminada", id);
    }

    private PrestamoDTO obtenerPrestamoRemoto(Long prestamoId) {
        try {
            return prestamoClient.obtenerPrestamoPorId(prestamoId);
        } catch (FeignException.NotFound e) {
            logger.warn("[MultaService] El prestamo con id={} no existe", prestamoId);
            throw new RuntimeException("El prestamo con id " + prestamoId + " no existe");
        } catch (FeignException e) {
            logger.error("[MultaService] Error al consultar microservicio-prestamos: {}", e.getMessage());
            throw new RuntimeException("No se pudo contactar al microservicio de prestamos");
        }
    }

    private UsuarioDTO obtenerUsuarioRemoto(Long usuarioId) {
        try {
            return usuarioClient.obtenerUsuarioPorId(usuarioId);
        } catch (FeignException.NotFound e) {
            logger.warn("[MultaService] El usuario con id={} no existe", usuarioId);
            throw new RuntimeException("El usuario con id " + usuarioId + " no existe");
        } catch (FeignException e) {
            logger.error("[MultaService] Error al consultar microservicio-usuarios: {}", e.getMessage());
            throw new RuntimeException("No se pudo contactar al microservicio de usuarios");
        }
    }
}
