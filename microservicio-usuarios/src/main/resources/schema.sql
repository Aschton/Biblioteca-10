-- Creacion manual de la tabla usuarios (ya no la genera Hibernate).
-- IF NOT EXISTS evita error si la tabla ya existe de un arranque anterior.
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    rut VARCHAR(50) NOT NULL,
    correo VARCHAR(255) NOT NULL
);
