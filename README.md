# Sistema de Gestión de Librería — Arquitectura de Microservicios

Proyecto Semestral de Arquitectura de Microservicios — DSY1103 Desarrollo FullStack 1
Examen Final Transversal (EFT)

## Integrantes

- Aschton Medolphe
- [Darliette Loconpan]

## Descripción del dominio

Sistema de gestión para una red de librerías/bibliotecas. Administra el catálogo de libros,
autores, editoriales y categorías; los usuarios y sus préstamos, reservas, multas y reseñas;
y las sucursales físicas. Cada entidad del dominio vive en su propio microservicio
independiente, con persistencia propia y comunicación vía REST.

## Microservicios implementados

| Microservicio | Puerto | Responsabilidad |
|---|---|---|
| microservicio-gateway | 8080 | API Gateway — punto de entrada único |
| microservicio-libros | 8081 | CRUD de libros y stock |
| microservicio-usuarios | 8082 | CRUD de usuarios |
| microservicio-prestamos | 8083 | Préstamos — valida libro y usuario vía Feign, controla stock |
| microservicio-autores | 8084 | CRUD de autores |
| microservicio-categorias | 8085 | CRUD de categorías |
| microservicio-editoriales | 8086 | CRUD de editoriales |
| microservicio-sucursales | 8087 | CRUD de sucursales |
| microservicio-reservas | 8088 | Reservas de libros — valida libro y usuario vía Feign |
| microservicio-multas | 8089 | Multas — valida préstamo y usuario vía Feign |
| microservicio-resenas | 8090 | Reseñas de libros — valida libro y usuario vía Feign |

## Arquitectura y stack técnico

- **Patrón CSR** (Controller–Service–Repository/Model) en todos los microservicios.
- **Persistencia**: JPA + Hibernate (`@Entity`, `JpaRepository`) sobre base de datos H2
  embebida (una base de datos independiente por microservicio, en `./data/<servicio>db`).
- **Validación**: Bean Validation (JSR 380) en los DTOs (`@NotBlank`, `@NotNull`, `@Email`,
  `@Min`, `@Pattern`).
- **Manejo de errores**: `@ControllerAdvice` centralizado por microservicio
  (`GlobalExceptionHandler`), con respuestas JSON consistentes y códigos HTTP adecuados.
- **Comunicación entre microservicios**: Feign Client (`prestamos`, `reservas`, `multas` y
  `resenas` consumen a `libros` y/o `usuarios`).
- **Documentación**: Swagger/OpenAPI (springdoc) en cada microservicio.
- **API Gateway**: Spring Cloud Gateway, enruta `/api/<recurso>/**` hacia cada microservicio,
  con un filtro global de logging (`LoggingGlobalFilter`).
- **Pruebas**: JUnit 5 + Mockito, con cobertura medida por JaCoCo (`mvn test` genera el
  reporte en `target/site/jacoco/index.html` de cada microservicio).
- **Configuración**: `application.properties` por microservicio (perfiles, puertos, URLs de
  Feign vía variables de entorno con valor por defecto en `localhost`).
- **Contenedores**: cada microservicio tiene su propio `Dockerfile`; `docker-compose.yml` en
  la raíz orquesta los 11 servicios en una red interna.

## Rutas principales del Gateway

Todas las rutas se acceden a través de `http://localhost:8080`:

```
/api/libros/**        -> microservicio-libros       (8081)
/api/usuarios/**      -> microservicio-usuarios      (8082)
/api/prestamos/**     -> microservicio-prestamos     (8083)
/api/autores/**       -> microservicio-autores       (8084)
/api/categorias/**    -> microservicio-categorias    (8085)
/api/editoriales/**   -> microservicio-editoriales   (8086)
/api/sucursales/**    -> microservicio-sucursales    (8087)
/api/reservas/**      -> microservicio-reservas      (8088)
/api/multas/**        -> microservicio-multas        (8089)
/api/resenas/**       -> microservicio-resenas       (8090)
```

## Documentación Swagger (local)

```
http://localhost:8081/swagger-ui.html   (libros)
http://localhost:8082/swagger-ui.html   (usuarios)
http://localhost:8083/swagger-ui.html   (prestamos)
http://localhost:8084/swagger-ui.html   (autores)
http://localhost:8085/swagger-ui.html   (categorias)
http://localhost:8086/swagger-ui.html   (editoriales)
http://localhost:8087/swagger-ui.html   (sucursales)
http://localhost:8088/swagger-ui.html   (reservas)
http://localhost:8089/swagger-ui.html   (multas)
http://localhost:8090/swagger-ui.html   (resenas)
```

## Ejecución local (desde el IDE o Maven)

Cada microservicio es un proyecto Maven independiente. Para levantar uno:

```bash
cd microservicio-libros
mvn spring-boot:run
```

Orden recomendado para tener toda la arquitectura funcionando en local:
`libros`, `usuarios` → `prestamos`, `reservas`, `multas`, `resenas` →
`autores`, `categorias`, `editoriales`, `sucursales` → `gateway`.

## Ejecución con Docker

Desde la raíz del repositorio:

```bash
docker compose up --build
```

Esto construye y levanta los 11 microservicios en una red interna de Docker; cada uno se
alcanza por su nombre de servicio (ej. `microservicio-libros`) en lugar de `localhost`, y
expone el mismo puerto hacia el host indicado en la tabla de arriba.

Para apagar todo:

```bash
docker compose down
```

## Pruebas unitarias y cobertura

Desde cada microservicio:

```bash
mvn test
```

Genera el reporte de cobertura JaCoCo en `target/site/jacoco/index.html`.

## Notas de diseño

- El `Prestamo` descuenta 1 unidad de stock del libro asociado (vía Feign PUT a
  `microservicio-libros`) al crearse en estado `ACTIVO`, y repone el stock cuando cambia a
  `DEVUELTO`.
- Cada microservicio administra su propia base de datos (H2 embebida en archivo), siguiendo
  el principio de independencia de datos entre microservicios.
