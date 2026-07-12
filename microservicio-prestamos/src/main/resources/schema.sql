-- Creacion manual de la tabla prestamos (ya no la genera Hibernate).
-- libro_id y usuario_id son solo columnas Long: la relacion con los otros
-- microservicios se valida en tiempo real via Feign (LibroClient/UsuarioClient),
-- no mediante claves foraneas de base de datos (cada microservicio tiene su propia BD).
CREATE TABLE IF NOT EXISTS prestamos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    libro_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    fecha_prestamo DATE,
    estado_prestamo VARCHAR(20) NOT NULL
);
