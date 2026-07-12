package com.libreria.microservicio_resenas.Service;

import com.libreria.microservicio_resenas.Client.LibroClient;
import com.libreria.microservicio_resenas.Client.UsuarioClient;
import com.libreria.microservicio_resenas.DTO.LibroDTO;
import com.libreria.microservicio_resenas.DTO.ResenaDTO;
import com.libreria.microservicio_resenas.DTO.UsuarioDTO;
import com.libreria.microservicio_resenas.Model.Resena;
import com.libreria.microservicio_resenas.Repository.ResenaRepository;
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
class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private LibroClient libroClient;

    @InjectMocks
    private ResenaService resenaService;

    private UsuarioDTO usuarioEjemplo() {
        UsuarioDTO u = new UsuarioDTO();
        u.setId(1L);
        u.setNombre("Juan Perez");
        return u;
    }

    private LibroDTO libroEjemplo() {
        LibroDTO l = new LibroDTO();
        l.setId(1L);
        l.setTitulo("El Quijote");
        return l;
    }

    private ResenaDTO resenaDTO() {
        ResenaDTO dto = new ResenaDTO();
        dto.setUsuarioId(1L);
        dto.setLibroId(1L);
        dto.setPuntuacion(5);
        dto.setComentario("Excelente libro");
        return dto;
    }

    @Test
    void crear_conUsuarioYLibroValidos_creaResena() {
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioEjemplo());
        when(libroClient.obtenerLibroPorId(1L)).thenReturn(libroEjemplo());
        when(resenaRepository.save(any(Resena.class))).thenAnswer(inv -> inv.getArgument(0));

        Resena creada = resenaService.crear(resenaDTO());

        assertEquals(5, creada.getPuntuacion());
        assertNotNull(creada.getFecha());
        verify(resenaRepository).save(any(Resena.class));
    }

    @Test
    void crear_cuandoUsuarioNoExiste_lanzaExcepcion() {
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenThrow(mock(FeignException.NotFound.class));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> resenaService.crear(resenaDTO()));
        assertTrue(ex.getMessage().contains("usuario"));
        verify(resenaRepository, never()).save(any());
    }

    @Test
    void crear_cuandoLibroNoExiste_lanzaExcepcion() {
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioEjemplo());
        when(libroClient.obtenerLibroPorId(1L)).thenThrow(mock(FeignException.NotFound.class));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> resenaService.crear(resenaDTO()));
        assertTrue(ex.getMessage().contains("libro"));
        verify(resenaRepository, never()).save(any());
    }

    @Test
    void eliminar_cuandoNoExiste_lanzaExcepcion() {
        when(resenaRepository.existsById(99L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> resenaService.eliminar(99L));
        verify(resenaRepository, never()).deleteById(any());
    }
}
