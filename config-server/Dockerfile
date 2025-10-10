# Etapa 1: build
FROM maven:3.9.11-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: runtime
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/config-server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8888
HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD curl -f http://localhost:8888/actuator/health || exit 1
ENTRYPOINT ["java","-jar","app.jar"]
