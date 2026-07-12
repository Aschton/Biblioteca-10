package com.libreria.microservicio_reservas.Service;

import com.libreria.microservicio_reservas.Client.LibroClient;
import com.libreria.microservicio_reservas.Client.UsuarioClient;
import com.libreria.microservicio_reservas.DTO.LibroDTO;
import com.libreria.microservicio_reservas.DTO.ReservaDTO;
import com.libreria.microservicio_reservas.DTO.UsuarioDTO;
import com.libreria.microservicio_reservas.Model.Reserva;
import com.libreria.microservicio_reservas.Repository.ReservaRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private LibroClient libroClient;

    @Autowired
    private UsuarioClient usuarioClient;

    public List<Reserva> obtenerTodos() {
        logger.info("[ReservaService] Obteniendo todas las reservas");
        return reservaRepository.findAll();
    }

    public Reserva obtenerPorId(Long id) {
        logger.info("[ReservaService] Buscando reserva con id={}", id);
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
    }

    public Reserva crear(ReservaDTO dto) {
        logger.info("[ReservaService] Creando reserva para libroId={} y usuarioId={}",
                dto.getLibroId(), dto.getUsuarioId());

        // 1) Verificar que el libro existe (Feign).
        LibroDTO libro = obtenerLibroRemoto(dto.getLibroId());
        logger.info("[ReservaService] Libro encontrado: {}", libro.getTitulo());

        // 2) Verificar que el usuario existe (Feign).
        UsuarioDTO usuario = obtenerUsuarioRemoto(dto.getUsuarioId());
        logger.info("[ReservaService] Usuario encontrado: {}", usuario.getNombre());

        // 3) Regla de negocio: una reserva PENDIENTE solo tiene sentido si el libro
        //    esta sin stock disponible (si hay stock, deberia hacer un prestamo directo).
        if ("PENDIENTE".equals(dto.getEstadoReserva())
                && libro.getStock() != null && libro.getStock() > 0) {
            throw new RuntimeException("El libro '" + libro.getTitulo() + "' tiene stock disponible, no es necesario reservar");
        }

        Reserva reserva = new Reserva();
        reserva.setLibroId(dto.getLibroId());
        reserva.setUsuarioId(dto.getUsuarioId());
        reserva.setFechaReserva(LocalDate.now());
        reserva.setEstadoReserva(dto.getEstadoReserva());

        Reserva guardada = reservaRepository.save(reserva);
        logger.info("[ReservaService] Reserva creada con id={}", guardada.getId());
        return guardada;
    }

    public Reserva actualizar(Long id, ReservaDTO dto) {
        logger.info("[ReservaService] Actualizando reserva con id={}", id);
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
        reserva.setLibroId(dto.getLibroId());
        reserva.setUsuarioId(dto.getUsuarioId());
        reserva.setEstadoReserva(dto.getEstadoReserva());
        Reserva actualizada = reservaRepository.save(reserva);
        logger.info("[ReservaService] Reserva id={} actualizada", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        logger.info("[ReservaService] Eliminando reserva con id={}", id);
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Reserva no encontrada con id: " + id);
        }
        reservaRepository.deleteById(id);
        logger.info("[ReservaService] Reserva id={} eliminada", id);
    }

    private LibroDTO obtenerLibroRemoto(Long libroId) {
        try {
            return libroClient.obtenerLibroPorId(libroId);
        } catch (FeignException.NotFound e) {
            logger.warn("[ReservaService] El libro con id={} no existe", libroId);
            throw new RuntimeException("El libro con id " + libroId + " no existe");
        } catch (FeignException e) {
            logger.error("[ReservaService] Error al consultar microservicio-libros: {}", e.getMessage());
            throw new RuntimeException("No se pudo contactar al microservicio de libros");
        }
    }

    private UsuarioDTO obtenerUsuarioRemoto(Long usuarioId) {
        try {
            return usuarioClient.obtenerUsuarioPorId(usuarioId);
        } catch (FeignException.NotFound e) {
            logger.warn("[ReservaService] El usuario con id={} no existe", usuarioId);
            throw new RuntimeException("El usuario con id " + usuarioId + " no existe");
        } catch (FeignException e) {
            logger.error("[ReservaService] Error al consultar microservicio-usuarios: {}", e.getMessage());
            throw new RuntimeException("No se pudo contactar al microservicio de usuarios");
        }
    }
}
