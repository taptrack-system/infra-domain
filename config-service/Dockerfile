# config-service/Dockerfile
FROM eclipse-temurin:25-jdk-alpine

# Diretório da aplicação dentro do container
WORKDIR /config

# Copia o jar gerado pelo Maven
COPY target/*.jar config.jar

# Expõe a porta do Eureka Server
EXPOSE 8888

# Comando para rodar a aplicação
ENTRYPOINT ["java","-jar","config.jar"]
