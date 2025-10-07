# Etapa 1: Build da aplicação
FROM maven:3.9.8-eclipse-temurin-25 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagem de execução
FROM eclipse-temurin:25-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/config-service-0.0.1-SNAPSHOT.jar app.jar

# Variáveis de ambiente
ENV SPRING_PROFILES_ACTIVE=dev
ENV SERVER_PORT=8888

EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
