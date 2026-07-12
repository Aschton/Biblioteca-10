package com.libreria.microservicio_usuarios.Service;

import com.libreria.microservicio_usuarios.DTO.UsuarioDTO;
import com.libreria.microservicio_usuarios.Model.Usuario;
import com.libreria.microservicio_usuarios.Repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Pruebas unitarias del UsuarioService con Mockito.
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioEjemplo() {
        return new Usuario(1L, "Juan Perez", "12345678-9", "juan@gmail.com");
    }

    private UsuarioDTO dtoEjemplo() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Juan Perez");
        dto.setRut("12345678-9");
        dto.setCorreo("juan@gmail.com");
        return dto;
    }

    @Test
    void obtenerPorId_cuandoExiste_devuelveUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo()));

        Usuario resultado = usuarioService.obtenerPorId(1L);

        assertEquals("Juan Perez", resultado.getNombre());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_lanzaExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.obtenerPorId(99L));
    }

    @Test
    void guardar_conCorreoNuevo_creaUsuario() {
        // Given: el correo no existe todavia
        UsuarioDTO dto = dtoEjemplo();
        when(usuarioRepository.existsByCorreo(dto.getCorreo())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Usuario guardado = usuarioService.guardar(dto);

        // Then
        assertEquals("juan@gmail.com", guardado.getCorreo());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void guardar_conCorreoDuplicado_lanzaExcepcion() {
        // Given: ya existe un usuario con ese correo (regla de negocio)
        UsuarioDTO dto = dtoEjemplo();
        when(usuarioRepository.existsByCorreo(dto.getCorreo())).thenReturn(true);

        // When + Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.guardar(dto));
        assertTrue(ex.getMessage().contains("correo"));
        // nunca debe guardar si el correo esta repetido
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void actualizar_cuandoExiste_actualizaCampos() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo()));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        UsuarioDTO dto = dtoEjemplo();
        dto.setNombre("Pedro Soto");

        Usuario actualizado = usuarioService.actualizar(1L, dto);

        assertEquals("Pedro Soto", actualizado.getNombre());
    }

    @Test
    void eliminar_cuandoNoExiste_lanzaExcepcion() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> usuarioService.eliminar(99L));
        verify(usuarioRepository, never()).deleteById(any());
    }
}
