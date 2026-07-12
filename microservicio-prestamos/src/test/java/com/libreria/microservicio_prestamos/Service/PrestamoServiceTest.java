package com.libreria.microservicio_prestamos.Service;

import com.libreria.microservicio_prestamos.Client.LibroClient;
import com.libreria.microservicio_prestamos.Client.UsuarioClient;
import com.libreria.microservicio_prestamos.DTO.LibroDTO;
import com.libreria.microservicio_prestamos.DTO.PrestamoDTO;
import com.libreria.microservicio_prestamos.DTO.UsuarioDTO;
import com.libreria.microservicio_prestamos.Model.Prestamo;
import com.libreria.microservicio_prestamos.Repository.PrestamoRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Pruebas unitarias del PrestamoService.
// Se simulan: el repositorio Y los dos clientes Feign (libros y usuarios),
// asi probamos la logica sin levantar los otros microservicios.
@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private LibroClient libroClient;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private PrestamoService prestamoService;

    private LibroDTO libroConStock(int stock) {
        LibroDTO libro = new LibroDTO();
        libro.setId(1L);
        libro.setTitulo("El Quijote");
        libro.setStock(stock);
        libro.setEstado("nuevo");
        return libro;
    }

    private UsuarioDTO usuarioEjemplo() {
        UsuarioDTO u = new UsuarioDTO();
        u.setId(1L);
        u.setNombre("Juan Perez");
        return u;
    }

    private PrestamoDTO prestamoDTO(String estado) {
        PrestamoDTO dto = new PrestamoDTO();
        dto.setLibroId(1L);
        dto.setUsuarioId(1L);
        dto.setEstadoPrestamo(estado);
        return dto;
    }

    @Test
    void crear_conLibroYUsuarioValidosYStock_creaPrestamo() {
        // Given: el libro existe con stock y el usuario existe
        when(libroClient.obtenerLibroPorId(1L)).thenReturn(libroConStock(5));
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioEjemplo());
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Prestamo creado = prestamoService.crear(prestamoDTO("ACTIVO"));

        // Then: se asigno la fecha y se guardo
        assertNotNull(creado.getFechaPrestamo());
        assertEquals("ACTIVO", creado.getEstadoPrestamo());
        verify(prestamoRepository).save(any(Prestamo.class));
    }

    @Test
    void crear_cuandoLibroNoExiste_lanzaExcepcion() {
        // Given: el microservicio de libros responde 404 (Feign lanza NotFound)
        when(libroClient.obtenerLibroPorId(1L)).thenThrow(mock(FeignException.NotFound.class));

        // When + Then
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> prestamoService.crear(prestamoDTO("ACTIVO")));
        assertTrue(ex.getMessage().contains("libro"));
        // si el libro no existe, nunca se guarda el prestamo
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    void crear_cuandoUsuarioNoExiste_lanzaExcepcion() {
        // Given: el libro existe, pero el usuario responde 404
        when(libroClient.obtenerLibroPorId(1L)).thenReturn(libroConStock(5));
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenThrow(mock(FeignException.NotFound.class));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> prestamoService.crear(prestamoDTO("ACTIVO")));
        assertTrue(ex.getMessage().contains("usuario"));
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    void crear_cuandoLibroSinStock_lanzaExcepcion() {
        // Given: libro y usuario existen, pero el libro tiene stock 0
        when(libroClient.obtenerLibroPorId(1L)).thenReturn(libroConStock(0));
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioEjemplo());

        // When + Then: la regla de negocio impide el prestamo ACTIVO
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> prestamoService.crear(prestamoDTO("ACTIVO")));
        assertTrue(ex.getMessage().contains("stock"));
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_lanzaExcepcion() {
        when(prestamoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> prestamoService.obtenerPorId(99L));
    }

    @Test
    void eliminar_cuandoExiste_borraPrestamo() {
        when(prestamoRepository.existsById(1L)).thenReturn(true);

        prestamoService.eliminar(1L);

        verify(prestamoRepository).deleteById(1L);
    }

    @Test
    void crear_conEstadoActivo_descuentaUnaUnidadDeStock() {
        // Given: libro con stock 5
        when(libroClient.obtenerLibroPorId(1L)).thenReturn(libroConStock(5));
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioEjemplo());
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        prestamoService.crear(prestamoDTO("ACTIVO"));

        // Then: se llamo a actualizarLibro con stock = 4
        verify(libroClient).actualizarLibro(eq(1L), argThat(dto -> dto.getStock() == 4));
    }

    @Test
    void actualizar_deActivoADevuelto_reponeUnaUnidadDeStock() {
        // Given: prestamo existente en estado ACTIVO
        Prestamo existente = new Prestamo();
        existente.setId(1L);
        existente.setLibroId(1L);
        existente.setUsuarioId(1L);
        existente.setEstadoPrestamo("ACTIVO");

        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(inv -> inv.getArgument(0));
        when(libroClient.obtenerLibroPorId(1L)).thenReturn(libroConStock(2));

        // When: se marca como DEVUELTO
        prestamoService.actualizar(1L, prestamoDTO("DEVUELTO"));

        // Then: el stock del libro sube en 1 (de 2 a 3)
        verify(libroClient).actualizarLibro(eq(1L), argThat(dto -> dto.getStock() == 3));
    }

    @Test
    void actualizar_deDevueltoAActivoSinStock_lanzaExcepcion() {
        // Given: prestamo DEVUELTO y libro sin stock disponible
        Prestamo existente = new Prestamo();
        existente.setId(1L);
        existente.setLibroId(1L);
        existente.setUsuarioId(1L);
        existente.setEstadoPrestamo("DEVUELTO");

        when(prestamoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(inv -> inv.getArgument(0));
        when(libroClient.obtenerLibroPorId(1L)).thenReturn(libroConStock(0));

        // When + Then: no se puede reactivar un prestamo si ya no hay stock
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> prestamoService.actualizar(1L, prestamoDTO("ACTIVO")));
        assertTrue(ex.getMessage().contains("stock"));
    }
}
