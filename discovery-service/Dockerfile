# Dockerfile (padrão para todos os serviços)
FROM eclipse-temurin:25-jdk-alpine

# Diretório de trabalho da aplicação
WORKDIR /app

# Copia o jar compilado para o container
COPY target/*.jar app.jar

# Variáveis de memória configuráveis (default)
ENV JAVA_OPTS="-Xms128m -Xmx512m"

# Expor a porta definida pelo service
EXPOSE ${APP_PORT}

# Comando de execução
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]