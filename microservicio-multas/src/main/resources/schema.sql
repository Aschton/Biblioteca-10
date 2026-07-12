CREATE TABLE IF NOT EXISTS multas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prestamo_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    monto DOUBLE NOT NULL,
    fecha_generacion DATE,
    estado_multa VARCHAR(20) NOT NULL
);
