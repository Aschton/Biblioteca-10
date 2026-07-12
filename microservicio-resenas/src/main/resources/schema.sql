CREATE TABLE IF NOT EXISTS resenas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    libro_id BIGINT NOT NULL,
    puntuacion INT NOT NULL,
    comentario VARCHAR(1000),
    fecha DATE
);
