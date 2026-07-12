# ===== Etapa 1: compilar el proyecto con Maven =====
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Copiamos primero el pom para aprovechar la cache de dependencias
COPY pom.xml .
COPY src ./src
# Empaquetamos el jar (saltamos los tests aqui para que el build sea mas rapido)
RUN mvn clean package -DskipTests

# ===== Etapa 2: ejecutar solo con el JRE (imagen mas liviana) =====
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copiamos el jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
