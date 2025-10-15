# Config Server
# Etapa 1: Build da aplicação
FROM maven:3.9.11-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/config-server-0.0.1-SNAPSHOT.jar app.jar

# Não copiamos config pois é apenas teste de integração
# COPY config /app/config

EXPOSE 8888

HEALTHCHECK --interval=30s --timeout=5s --retries=5 --start-period=60s \
  CMD curl -f http://localhost:8888/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]

