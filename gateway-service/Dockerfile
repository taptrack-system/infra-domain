# gateway-service/Dockerfile
FROM eclipse-temurin:25-jdk-alpine

WORKDIR /gateway

COPY target/*.jar gateway.jar

# Porta do Gateway
EXPOSE 8080

ENTRYPOINT ["java","-jar","gateway.jar"]
