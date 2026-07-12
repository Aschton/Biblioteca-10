package com.libreria.microservicio_multas.Service;

import com.libreria.microservicio_multas.Client.PrestamoClient;
import com.libreria.microservicio_multas.Client.UsuarioClient;
import com.libreria.microservicio_multas.DTO.MultaDTO;
import com.libreria.microservicio_multas.DTO.PrestamoDTO;
import com.libreria.microservicio_multas.DTO.UsuarioDTO;
import com.libreria.microservicio_multas.Model.Multa;
import com.libreria.microservicio_multas.Repository.MultaRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MultaServiceTest {

    @Mock
    private MultaRepository multaRepository;

    @Mock
    private PrestamoClient prestamoClient;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private MultaService multaService;

    private PrestamoDTO prestamoEjemplo() {
        PrestamoDTO p = new PrestamoDTO();
        p.setId(1L);
        p.setUsuarioId(1L);
        p.setEstadoPrestamo("ACTIVO");
        return p;
    }

    private UsuarioDTO usuarioEjemplo() {
        UsuarioDTO u = new UsuarioDTO();
        u.setId(1L);
        u.setNombre("Juan Perez");
        return u;
    }

    private MultaDTO multaDTO() {
        MultaDTO dto = new MultaDTO();
        dto.setPrestamoId(1L);
        dto.setUsuarioId(1L);
        dto.setMonto(1500.0);
        dto.setEstadoMulta("PENDIENTE");
        return dto;
    }

    @Test
    void crear_conPrestamoYUsuarioValidos_creaMulta() {
        when(prestamoClient.obtenerPrestamoPorId(1L)).thenReturn(prestamoEjemplo());
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioEjemplo());
        when(multaRepository.save(any(Multa.class))).thenAnswer(inv -> inv.getArgument(0));

        Multa creada = multaService.crear(multaDTO());

        assertEquals(1500.0, creada.getMonto());
        assertEquals("PENDIENTE", creada.getEstadoMulta());
        verify(multaRepository).save(any(Multa.class));
    }

    @Test
    void crear_cuandoPrestamoNoExiste_lanzaExcepcion() {
        when(prestamoClient.obtenerPrestamoPorId(1L)).thenThrow(mock(FeignException.NotFound.class));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> multaService.crear(multaDTO()));
        assertTrue(ex.getMessage().contains("prestamo"));
        verify(multaRepository, never()).save(any());
    }

    @Test
    void crear_cuandoUsuarioNoCoincideConPrestamo_lanzaExcepcion() {
        PrestamoDTO prestamoDeOtroUsuario = prestamoEjemplo();
        prestamoDeOtroUsuario.setUsuarioId(99L);
        when(prestamoClient.obtenerPrestamoPorId(1L)).thenReturn(prestamoDeOtroUsuario);
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioEjemplo());

        assertThrows(RuntimeException.class, () -> multaService.crear(multaDTO()));
        verify(multaRepository, never()).save(any());
    }

    @Test
    void eliminar_cuandoNoExiste_lanzaExcepcion() {
        when(multaRepository.existsById(99L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> multaService.eliminar(99L));
        verify(multaRepository, never()).deleteById(any());
    }
}
