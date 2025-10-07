# Eureka Server

## Estrutura

```text
eureka-server/
├── Dockerfile
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── infradomain
    │   │           └── eurekaserver
    │   │               └── EurekaServerApplication.java
    │   └── resources
    │       ├── application.yml
    │       └── logback-spring.xml
    └── test
        └── java
            └── com
                └── infradomain
                    └── eurekaserver
                        └── EurekaServerApplicationTests.java
```

## Executar

### Execução Local

```bash
# Build jar
mvn clean package -DskipTests

# Executar localmente
java -jar target/eureka-server-0.0.1-SNAPSHOT.jar
```

Acesse: http://localhost:8761

### Execução via Docker

```bash
docker build -t eureka-server .
docker run -d -p 8761:8761 --name eureka eureka-server
```

## Conectando com outros serviços

Quando o Gateway ou outros microsserviços forem criados, devem incluir no `application.yml`:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://eureka:8761/eureka
```