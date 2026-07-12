package com.libreria.microservicio_editoriales.Service;

import com.libreria.microservicio_editoriales.DTO.EditorialDTO;
import com.libreria.microservicio_editoriales.Model.Editorial;
import com.libreria.microservicio_editoriales.Repository.EditorialRepository;
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
class EditorialServiceTest {

    @Mock
    private EditorialRepository editorialRepository;

    @InjectMocks
    private EditorialService editorialService;

    private Editorial editorialEjemplo() {
        return new Editorial(1L, "Sudamericana", "Argentina");
    }

    private EditorialDTO dtoEjemplo() {
        EditorialDTO dto = new EditorialDTO();
        dto.setNombre("Sudamericana");
        dto.setPais("Argentina");
        return dto;
    }

    @Test
    void obtenerPorId_cuandoExiste_devuelveEditorial() {
        when(editorialRepository.findById(1L)).thenReturn(Optional.of(editorialEjemplo()));
        Editorial resultado = editorialService.obtenerPorId(1L);
        assertEquals("Sudamericana", resultado.getNombre());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_lanzaExcepcion() {
        when(editorialRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> editorialService.obtenerPorId(99L));
    }

    @Test
    void guardar_creaYDevuelveEditorial() {
        when(editorialRepository.save(any(Editorial.class))).thenAnswer(inv -> inv.getArgument(0));
        Editorial guardada = editorialService.guardar(dtoEjemplo());
        assertEquals("Argentina", guardada.getPais());
        verify(editorialRepository).save(any(Editorial.class));
    }

    @Test
    void actualizar_cuandoExiste_actualizaCampos() {
        when(editorialRepository.findById(1L)).thenReturn(Optional.of(editorialEjemplo()));
        when(editorialRepository.save(any(Editorial.class))).thenAnswer(inv -> inv.getArgument(0));
        EditorialDTO dto = dtoEjemplo();
        dto.setNombre("Planeta");
        Editorial actualizada = editorialService.actualizar(1L, dto);
        assertEquals("Planeta", actualizada.getNombre());
    }

    @Test
    void eliminar_cuandoNoExiste_lanzaExcepcion() {
        when(editorialRepository.existsById(99L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> editorialService.eliminar(99L));
        verify(editorialRepository, never()).deleteById(any());
    }

    @Test
    void obtenerTodos_devuelveLista() {
        when(editorialRepository.findAll()).thenReturn(List.of(editorialEjemplo()));
        List<Editorial> lista = editorialService.obtenerTodos();
        assertEquals(1, lista.size());
    }
}
