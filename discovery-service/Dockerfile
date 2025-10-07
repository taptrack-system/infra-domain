# Stage 1 - build
FROM maven:3.9.8-eclipse-temurin-25 AS builder
WORKDIR /workspace/app

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests clean package

# Stage 2 - runtime
FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=builder /workspace/app/target/discovery-service-0.0.1-SNAPSHOT.jar ./discovery-service.jar

ENV SPRING_PROFILES_ACTIVE=dev
EXPOSE 8761

ENTRYPOINT ["java","-jar","/app/discovery-service.jar"]
