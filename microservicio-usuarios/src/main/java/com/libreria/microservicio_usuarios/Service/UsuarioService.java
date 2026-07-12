package com.libreria.microservicio_usuarios.Service;

import com.libreria.microservicio_usuarios.DTO.UsuarioDTO;
import com.libreria.microservicio_usuarios.Model.Usuario;
import com.libreria.microservicio_usuarios.Repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerTodos() {
        logger.info("[UsuarioService] Obteniendo todos los usuarios");
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        logger.info("[UsuarioService] Buscando usuario con id={}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public Usuario guardar(UsuarioDTO dto) {
        logger.info("[UsuarioService] Creando usuario: {}", dto.getNombre());

        // Regla de negocio: no se permite registrar dos usuarios con el mismo correo.
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("Ya existe un usuario con el correo: " + dto.getCorreo());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setRut(dto.getRut());
        usuario.setCorreo(dto.getCorreo());
        Usuario guardado = usuarioRepository.save(usuario);
        logger.info("[UsuarioService] Usuario creado con id={}", guardado.getId());
        return guardado;
    }

    public Usuario actualizar(Long id, UsuarioDTO dto) {
        logger.info("[UsuarioService] Actualizando usuario con id={}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuario.setNombre(dto.getNombre());
        usuario.setRut(dto.getRut());
        usuario.setCorreo(dto.getCorreo());
        Usuario actualizado = usuarioRepository.save(usuario);
        logger.info("[UsuarioService] Usuario id={} actualizado", id);
        return actualizado;
    }

    public void eliminar(Long id) {
        logger.info("[UsuarioService] Eliminando usuario con id={}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
        logger.info("[UsuarioService] Usuario id={} eliminado", id);
    }
}
