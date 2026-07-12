package com.libreria.microservicio_categorias.Service;

import com.libreria.microservicio_categorias.DTO.CategoriaDTO;
import com.libreria.microservicio_categorias.Model.Categoria;
import com.libreria.microservicio_categorias.Repository.CategoriaRepository;
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
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoriaEjemplo() {
        return new Categoria(1L, "Ciencia Ficcion", "Libros de ciencia ficcion y futurismo");
    }

    private CategoriaDTO dtoEjemplo() {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setNombre("Ciencia Ficcion");
        dto.setDescripcion("Libros de ciencia ficcion y futurismo");
        return dto;
    }

    @Test
    void obtenerPorId_cuandoExiste_devuelveCategoria() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaEjemplo()));
        Categoria resultado = categoriaService.obtenerPorId(1L);
        assertEquals("Ciencia Ficcion", resultado.getNombre());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_lanzaExcepcion() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> categoriaService.obtenerPorId(99L));
    }

    @Test
    void guardar_creaYDevuelveCategoria() {
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> inv.getArgument(0));
        Categoria guardada = categoriaService.guardar(dtoEjemplo());
        assertEquals("Ciencia Ficcion", guardada.getNombre());
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void actualizar_cuandoExiste_actualizaCampos() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaEjemplo()));
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(inv -> inv.getArgument(0));
        CategoriaDTO dto = dtoEjemplo();
        dto.setNombre("Fantasia");
        Categoria actualizada = categoriaService.actualizar(1L, dto);
        assertEquals("Fantasia", actualizada.getNombre());
    }

    @Test
    void eliminar_cuandoNoExiste_lanzaExcepcion() {
        when(categoriaRepository.existsById(99L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> categoriaService.eliminar(99L));
        verify(categoriaRepository, never()).deleteById(any());
    }

    @Test
    void obtenerTodos_devuelveLista() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoriaEjemplo()));
        List<Categoria> lista = categoriaService.obtenerTodos();
        assertEquals(1, lista.size());
    }
}
