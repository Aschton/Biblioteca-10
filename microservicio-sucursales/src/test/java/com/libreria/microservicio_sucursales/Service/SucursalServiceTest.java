package com.libreria.microservicio_sucursales.Service;

import com.libreria.microservicio_sucursales.DTO.SucursalDTO;
import com.libreria.microservicio_sucursales.Model.Sucursal;
import com.libreria.microservicio_sucursales.Repository.SucursalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalService sucursalService;

    private Sucursal sucursalEjemplo() {
        return new Sucursal(1L, "Sucursal Centro", "Av. Principal 123", "+56912345678");
    }

    private SucursalDTO dtoEjemplo() {
        SucursalDTO dto = new SucursalDTO();
        dto.setNombre("Sucursal Centro");
        dto.setDireccion("Av. Principal 123");
        dto.setTelefono("+56912345678");
        return dto;
    }

    @Test
    void obtenerPorId_cuandoExiste_devuelveSucursal() {
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursalEjemplo()));
        Sucursal resultado = sucursalService.obtenerPorId(1L);
        assertEquals("Sucursal Centro", resultado.getNombre());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_lanzaExcepcion() {
        when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> sucursalService.obtenerPorId(99L));
    }

    @Test
    void guardar_creaYDevuelveSucursal() {
        when(sucursalRepository.save(any(Sucursal.class))).thenAnswer(inv -> inv.getArgument(0));
        Sucursal guardada = sucursalService.guardar(dtoEjemplo());
        assertEquals("Sucursal Centro", guardada.getNombre());
        verify(sucursalRepository).save(any(Sucursal.class));
    }

    @Test
    void actualizar_cuandoExiste_actualizaCampos() {
        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursalEjemplo()));
        when(sucursalRepository.save(any(Sucursal.class))).thenAnswer(inv -> inv.getArgument(0));
        SucursalDTO dto = dtoEjemplo();
        dto.setNombre("Sucursal Norte");
        Sucursal actualizada = sucursalService.actualizar(1L, dto);
        assertEquals("Sucursal Norte", actualizada.getNombre());
    }

    @Test
    void eliminar_cuandoNoExiste_lanzaExcepcion() {
        when(sucursalRepository.existsById(99L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> sucursalService.eliminar(99L));
        verify(sucursalRepository, never()).deleteById(any());
    }

    @Test
    void obtenerTodos_devuelveLista() {
        when(sucursalRepository.findAll()).thenReturn(List.of(sucursalEjemplo()));
        List<Sucursal> lista = sucursalService.obtenerTodos();
        assertEquals(1, lista.size());
    }
}
