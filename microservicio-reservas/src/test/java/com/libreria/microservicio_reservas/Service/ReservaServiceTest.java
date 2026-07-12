package com.libreria.microservicio_reservas.Service;

import com.libreria.microservicio_reservas.Client.LibroClient;
import com.libreria.microservicio_reservas.Client.UsuarioClient;
import com.libreria.microservicio_reservas.DTO.LibroDTO;
import com.libreria.microservicio_reservas.DTO.ReservaDTO;
import com.libreria.microservicio_reservas.DTO.UsuarioDTO;
import com.libreria.microservicio_reservas.Model.Reserva;
import com.libreria.microservicio_reservas.Repository.ReservaRepository;
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
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private LibroClient libroClient;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private ReservaService reservaService;

    private LibroDTO libroSinStock() {
        LibroDTO libro = new LibroDTO();
        libro.setId(1L);
        libro.setTitulo("El Quijote");
        libro.setStock(0);
        return libro;
    }

    private UsuarioDTO usuarioEjemplo() {
        UsuarioDTO u = new UsuarioDTO();
        u.setId(1L);
        u.setNombre("Juan Perez");
        return u;
    }

    private ReservaDTO reservaDTO(String estado) {
        ReservaDTO dto = new ReservaDTO();
        dto.setLibroId(1L);
        dto.setUsuarioId(1L);
        dto.setEstadoReserva(estado);
        return dto;
    }

    @Test
    void crear_conLibroSinStockYUsuarioValido_creaReserva() {
        when(libroClient.obtenerLibroPorId(1L)).thenReturn(libroSinStock());
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioEjemplo());
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        Reserva creada = reservaService.crear(reservaDTO("PENDIENTE"));

        assertNotNull(creada.getFechaReserva());
        assertEquals("PENDIENTE", creada.getEstadoReserva());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void crear_cuandoLibroNoExiste_lanzaExcepcion() {
        when(libroClient.obtenerLibroPorId(1L)).thenThrow(mock(FeignException.NotFound.class));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reservaService.crear(reservaDTO("PENDIENTE")));
        assertTrue(ex.getMessage().contains("libro"));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_lanzaExcepcion() {
        when(reservaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> reservaService.obtenerPorId(99L));
    }

    @Test
    void eliminar_cuandoExiste_borraReserva() {
        when(reservaRepository.existsById(1L)).thenReturn(true);
        reservaService.eliminar(1L);
        verify(reservaRepository).deleteById(1L);
    }
}
