package com.libreria.microservicio_libros.Service;

import com.libreria.microservicio_libros.DTO.LibroDTO;
import com.libreria.microservicio_libros.Model.Libro;
import com.libreria.microservicio_libros.Repository.LibroRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Pruebas unitarias del LibroService.
// Usamos Mockito para simular el repositorio: NO se toca la base de datos real.
@ExtendWith(MockitoExtension.class)
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository; // dependencia simulada (mock)

    @InjectMocks
    private LibroService libroService; // clase bajo prueba, con el mock inyectado

    // Metodo de apoyo para construir un libro de ejemplo.
    private Libro libroEjemplo() {
        return new Libro(1L, "El Quijote", "Cervantes", 5, "nuevo");
    }

    @Test
    void obtenerPorId_cuandoExiste_devuelveLibro() {
        // Given: el repositorio encuentra el libro con id 1
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libroEjemplo()));

        // When: pedimos el libro por id
        Libro resultado = libroService.obtenerPorId(1L);

        // Then: se devuelve el libro esperado
        assertNotNull(resultado);
        assertEquals("El Quijote", resultado.getTitulo());
        verify(libroRepository).findById(1L);
    }

    @Test
    void obtenerPorId_cuandoNoExiste_lanzaExcepcion() {
        // Given: el repositorio no encuentra nada
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        // When + Then: debe lanzar una excepcion
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> libroService.obtenerPorId(99L));
        assertTrue(ex.getMessage().contains("no encontrado"));
    }

    @Test
    void guardar_creaYDevuelveLibro() {
        // Given: un DTO de entrada
        LibroDTO dto = new LibroDTO();
        dto.setTitulo("Cien anios de soledad");
        dto.setAutor("Garcia Marquez");
        dto.setStock(3);
        dto.setEstado("nuevo");
        // el repositorio devuelve lo que recibe al guardar
        when(libroRepository.save(any(Libro.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Libro guardado = libroService.guardar(dto);

        // Then: los campos del DTO se copiaron al libro guardado
        assertEquals("Cien anios de soledad", guardado.getTitulo());
        assertEquals(3, guardado.getStock());
        verify(libroRepository).save(any(Libro.class));
    }

    @Test
    void actualizar_cuandoExiste_actualizaCampos() {
        // Given
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libroEjemplo()));
        when(libroRepository.save(any(Libro.class))).thenAnswer(inv -> inv.getArgument(0));
        LibroDTO dto = new LibroDTO();
        dto.setTitulo("Titulo nuevo");
        dto.setAutor("Autor nuevo");
        dto.setStock(10);
        dto.setEstado("usado");

        // When
        Libro actualizado = libroService.actualizar(1L, dto);

        // Then
        assertEquals("Titulo nuevo", actualizado.getTitulo());
        assertEquals(10, actualizado.getStock());
        assertEquals("usado", actualizado.getEstado());
    }

    @Test
    void actualizar_cuandoNoExiste_lanzaExcepcion() {
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());
        LibroDTO dto = new LibroDTO();

        assertThrows(RuntimeException.class, () -> libroService.actualizar(99L, dto));
        verify(libroRepository, never()).save(any());
    }

    @Test
    void eliminar_cuandoExiste_borraLibro() {
        // Given: el libro existe
        when(libroRepository.existsById(1L)).thenReturn(true);

        // When
        libroService.eliminar(1L);

        // Then: se llamo a deleteById
        verify(libroRepository).deleteById(1L);
    }

    @Test
    void eliminar_cuandoNoExiste_lanzaExcepcion() {
        when(libroRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> libroService.eliminar(99L));
        verify(libroRepository, never()).deleteById(any());
    }

    @Test
    void obtenerTodos_devuelveLista() {
        when(libroRepository.findAll()).thenReturn(List.of(libroEjemplo()));

        List<Libro> lista = libroService.obtenerTodos();

        assertEquals(1, lista.size());
        verify(libroRepository).findAll();
    }
}
