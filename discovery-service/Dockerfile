# discovery-service/Dockerfile
FROM eclipse-temurin:25-jdk-alpine

# Diretório da aplicação dentro do container
WORKDIR /discovery

# Copia o jar gerado pelo Maven
COPY target/*.jar discovery.jar

# Expõe a porta do Eureka Server
EXPOSE 8761

# Comando para rodar a aplicação
ENTRYPOINT ["java","-jar","discovery.jar"]
