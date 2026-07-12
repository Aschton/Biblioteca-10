package com.libreria.microservicio_sucursales.Service;

import com.libreria.microservicio_sucursales.DTO.SucursalDTO;
import com.libreria.microservicio_sucursales.Model.Sucursal;
import com.libreria.microservicio_sucursales.Repository.SucursalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SucursalService {

    private static final Logger logger = LoggerFactory.getLogger(SucursalService.class);

    @Autowired
    private SucursalRepository sucursalRepository;

    public List<Sucursal> obtenerTodos() {
        logger.info("[SucursalService] Obteniendo todas las sucursales");
        return sucursalRepository.findAll();
    }

    public Sucursal obtenerPorId(Long id) {
        logger.info("[SucursalService] Buscando sucursal con id={}", id);
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con id: " + id));
    }

    public Sucursal guardar(SucursalDTO dto) {
        logger.info("[SucursalService] Creando sucursal: {}", dto.getNombre());
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(dto.getNombre());
        sucursal.setDireccion(dto.getDireccion());
        sucursal.setTelefono(dto.getTelefono());
        Sucursal guardada = sucursalRepository.save(sucursal);
        logger.info("[SucursalService] Sucursal creada con id={}", guardada.getId());
        return guardada;
    }

    public Sucursal actualizar(Long id, SucursalDTO dto) {
        logger.info("[SucursalService] Actualizando sucursal con id={}", id);
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con id: " + id));
        sucursal.setNombre(dto.getNombre());
        sucursal.setDireccion(dto.getDireccion());
        sucursal.setTelefono(dto.getTelefono());
        Sucursal actualizada = sucursalRepository.save(sucursal);
        logger.info("[SucursalService] Sucursal id={} actualizada", id);
        return actualizada;
    }

    public void eliminar(Long id) {
        logger.info("[SucursalService] Eliminando sucursal con id={}", id);
        if (!sucursalRepository.existsById(id)) {
            throw new RuntimeException("Sucursal no encontrada con id: " + id);
        }
        sucursalRepository.deleteById(id);
        logger.info("[SucursalService] Sucursal id={} eliminada", id);
    }
}
