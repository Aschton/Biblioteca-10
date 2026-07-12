-- Creacion manual de la tabla libros (ya no la genera Hibernate).
CREATE TABLE IF NOT EXISTS libros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    stock INT NOT NULL,
    estado VARCHAR(20) NOT NULL
);
