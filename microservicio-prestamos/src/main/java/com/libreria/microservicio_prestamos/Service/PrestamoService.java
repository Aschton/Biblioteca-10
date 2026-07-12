package com.libreria.microservicio_prestamos.Service;

import com.libreria.microservicio_prestamos.Client.LibroClient;
import com.libreria.microservicio_prestamos.Client.UsuarioClient;
import com.libreria.microservicio_prestamos.DTO.LibroDTO;
import com.libreria.microservicio_prestamos.DTO.PrestamoDTO;
import com.libreria.microservicio_prestamos.DTO.UsuarioDTO;
import com.libreria.microservicio_prestamos.Model.Prestamo;
import com.libreria.microservicio_prestamos.Repository.PrestamoRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private static final Logger logger = LoggerFactory.getLogger(PrestamoService.class);

    @Autowired
    private PrestamoRepository prestamoRepository;

    // Feign Clients para comunicarse con los otros microservicios
    @Autowired
    private LibroClient libroClient;

    @Autowired
    private UsuarioClient usuarioClient;

    public List<Prestamo> obtenerTodos() {
        logger.info("[PrestamoService] Obteniendo todos los prestamos");
        return prestamoRepository.findAll();
    }

    public Prestamo obtenerPorId(Long id) {
        logger.info("[PrestamoService] Buscando prestamo con id={}", id);
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestamo no encontrado con id: " + id));
    }

    public Prestamo crear(PrestamoDTO dto) {
        logger.info("[PrestamoService] Creando prestamo para libroId={} y usuarioId={}",
                dto.getLibroId(), dto.getUsuarioId());

        // 1) Verificar que el libro existe (llamada REST remota via Feign).
        LibroDTO libro = obtenerLibroRemoto(dto.getLibroId());
        logger.info("[PrestamoService] Libro encontrado: {}", libro.getTitulo());

        // 2) Verificar que el usuario existe (llamada REST remota via Feign).
        UsuarioDTO usuario = obtenerUsuarioRemoto(dto.getUsuarioId());
        logger.info("[PrestamoService] Usuario encontrado: {}", usuario.getNombre());

        // 3) Regla de negocio: para prestar (ACTIVO) el libro debe tener stock disponible.
        if ("ACTIVO".equals(dto.getEstadoPrestamo())
                && (libro.getStock() == null || libro.getStock() <= 0)) {
            throw new RuntimeException("El libro '" + libro.getTitulo() + "' no tiene stock disponible");
        }

        // 4) Crear y guardar el prestamo.
        Prestamo prestamo = new Prestamo();
        prestamo.setLibroId(dto.getLibroId());
        prestamo.setUsuarioId(dto.getUsuarioId());
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setEstadoPrestamo(dto.getEstadoPrestamo());

        Prestamo guardado = prestamoRepository.save(prestamo);
        logger.info("[PrestamoService] Prestamo creado con id={}", guardado.getId());

        // 5) Si el prestamo queda ACTIVO, se descuenta 1 unidad del stock del libro
        //    en microservicio-libros (comunicacion remota via Feign, PUT).
        if ("ACTIVO".equals(dto.getEstadoPrestamo())) {
            actualizarStockLibro(libro, -1);
        }

        return guardado;
    }

    public Prestamo actualizar(Long id, PrestamoDTO dto) {
        logger.info("[PrestamoService] Actualizando prestamo con id={}", id);
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestamo no encontrado con id: " + id));

        String estadoAnterior = prestamo.getEstadoPrestamo();
        String estadoNuevo = dto.getEstadoPrestamo();

        prestamo.setLibroId(dto.getLibroId());
        prestamo.setUsuarioId(dto.getUsuarioId());
        prestamo.setEstadoPrestamo(estadoNuevo);
        Prestamo actualizado = prestamoRepository.save(prestamo);
        logger.info("[PrestamoService] Prestamo id={} actualizado", id);

        // Regla de negocio: si el prestamo pasa de ACTIVO a DEVUELTO, se repone
        // 1 unidad de stock en microservicio-libros. Si pasa de DEVUELTO a ACTIVO
        // (reactivacion), se vuelve a descontar.
        if ("ACTIVO".equals(estadoAnterior) && "DEVUELTO".equals(estadoNuevo)) {
            LibroDTO libro = obtenerLibroRemoto(prestamo.getLibroId());
            actualizarStockLibro(libro, +1);
        } else if ("DEVUELTO".equals(estadoAnterior) && "ACTIVO".equals(estadoNuevo)) {
            LibroDTO libro = obtenerLibroRemoto(prestamo.getLibroId());
            if (libro.getStock() == null || libro.getStock() <= 0) {
                throw new RuntimeException("El libro '" + libro.getTitulo() + "' no tiene stock disponible");
            }
            actualizarStockLibro(libro, -1);
        }

        return actualizado;
    }

    public void eliminar(Long id) {
        logger.info("[PrestamoService] Eliminando prestamo con id={}", id);
        if (!prestamoRepository.existsById(id)) {
            throw new RuntimeException("Prestamo no encontrado con id: " + id);
        }
        prestamoRepository.deleteById(id);
        logger.info("[PrestamoService] Prestamo id={} eliminado", id);
    }

    // --- Metodos privados que aislan el manejo de errores remotos (Feign) ---

    // Si el microservicio responde 404, Feign lanza FeignException.NotFound (NO devuelve null).
    // Si el microservicio esta caido, Feign lanza una FeignException general.
    private LibroDTO obtenerLibroRemoto(Long libroId) {
        try {
            return libroClient.obtenerLibroPorId(libroId);
        } catch (FeignException.NotFound e) {
            logger.warn("[PrestamoService] El libro con id={} no existe", libroId);
            throw new RuntimeException("El libro con id " + libroId + " no existe");
        } catch (FeignException e) {
            logger.error("[PrestamoService] Error al consultar microservicio-libros: {}", e.getMessage());
            throw new RuntimeException("No se pudo contactar al microservicio de libros");
        }
    }

    private UsuarioDTO obtenerUsuarioRemoto(Long usuarioId) {
        try {
            return usuarioClient.obtenerUsuarioPorId(usuarioId);
        } catch (FeignException.NotFound e) {
            logger.warn("[PrestamoService] El usuario con id={} no existe", usuarioId);
            throw new RuntimeException("El usuario con id " + usuarioId + " no existe");
        } catch (FeignException e) {
            logger.error("[PrestamoService] Error al consultar microservicio-usuarios: {}", e.getMessage());
            throw new RuntimeException("No se pudo contactar al microservicio de usuarios");
        }
    }

    // Ajusta el stock de un libro en microservicio-libros via PUT remoto (Feign).
    // delta = -1 al prestar, +1 al devolver.
    private void actualizarStockLibro(LibroDTO libro, int delta) {
        LibroDTO actualizacion = new LibroDTO();
        actualizacion.setTitulo(libro.getTitulo());
        actualizacion.setAutor(libro.getAutor());
        actualizacion.setEstado(libro.getEstado());
        actualizacion.setStock(libro.getStock() + delta);
        try {
            libroClient.actualizarLibro(libro.getId(), actualizacion);
            logger.info("[PrestamoService] Stock del libro id={} ajustado en {}", libro.getId(), delta);
        } catch (FeignException e) {
            logger.error("[PrestamoService] No se pudo actualizar el stock del libro id={}: {}",
                    libro.getId(), e.getMessage());
            throw new RuntimeException("No se pudo actualizar el stock del libro");
        }
    }
}
