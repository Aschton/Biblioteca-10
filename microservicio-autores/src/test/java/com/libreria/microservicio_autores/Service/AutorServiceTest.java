package com.libreria.microservicio_autores.Service;

import com.libreria.microservicio_autores.DTO.AutorDTO;
import com.libreria.microservicio_autores.Model.Autor;
import com.libreria.microservicio_autores.Repository.AutorRepository;
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
class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private AutorService autorService;

    private Autor autorEjemplo() {
        return new Autor(1L, "Gabriel Garcia Marquez", "Colombiana", "Escritor y periodista");
    }

    private AutorDTO dtoEjemplo() {
        AutorDTO dto = new AutorDTO();
        dto.setNombre("Gabriel Garcia Marquez");
        dto.setNacionalidad("Colombiana");
        dto.setBiografia("Escritor y periodista");
        return dto;
    }

    @Test
    void obtenerPorId_cuandoExiste_devuelveAutor() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autorEjemplo()));
        Autor resultado = autorService.obtenerPorId(1L);
        assertEquals("Gabriel Garcia Marquez", resultado.getNombre());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_lanzaExcepcion() {
        when(autorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> autorService.obtenerPorId(99L));
    }

    @Test
    void guardar_creaYDevuelveAutor() {
        when(autorRepository.save(any(Autor.class))).thenAnswer(inv -> inv.getArgument(0));
        Autor guardado = autorService.guardar(dtoEjemplo());
        assertEquals("Colombiana", guardado.getNacionalidad());
        verify(autorRepository).save(any(Autor.class));
    }

    @Test
    void actualizar_cuandoExiste_actualizaCampos() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autorEjemplo()));
        when(autorRepository.save(any(Autor.class))).thenAnswer(inv -> inv.getArgument(0));
        AutorDTO dto = dtoEjemplo();
        dto.setNombre("Mario Vargas Llosa");
        Autor actualizado = autorService.actualizar(1L, dto);
        assertEquals("Mario Vargas Llosa", actualizado.getNombre());
    }

    @Test
    void eliminar_cuandoNoExiste_lanzaExcepcion() {
        when(autorRepository.existsById(99L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> autorService.eliminar(99L));
        verify(autorRepository, never()).deleteById(any());
    }

    @Test
    void obtenerTodos_devuelveLista() {
        when(autorRepository.findAll()).thenReturn(List.of(autorEjemplo()));
        List<Autor> lista = autorService.obtenerTodos();
        assertEquals(1, lista.size());
    }
}
