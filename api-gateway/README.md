# API Gateway

API Gateway para roteamento e controle de acesso aos microsserviços do ecossistema.

## Estrutura do Projeto

```text
gateway-service/                    # porta: 8080 | Eureka Client
├─ src/
│  ├─ main/
│  │  ├─ java/com/example/gateway/
│  │  │  └─ GatewayServiceApplication.java
│  │  └─ resources/
│  │     ├─ application.yml
│  │     └─ bootstrap.yml
├─ Dockerfile
├─ pom.xml
└─ README.md
```

---

## Stack

- Java 25
- Spring Boot 3.5.6
- Spring Cloud 2025.0.0
- Spring Cloud Gateway (Reactive)
- Eureka Discovery Client
- Config Client
- Actuator

--- 

## Configuração de portas

| Serviço                   | Porta | Descrição                  |
|---------------------------|-------|----------------------------|
| Config Server             | 8888  | Central de configuração    |
| Eureka Server             | 8761  | Registro de serviços       |
| API Gateway               | 8080  | Gateway de entrada         |
| Identity Profiles Service | 8081  | Gestão de perfis           |
| Auth Service (futuro)     | 8082  | Autenticação e autorização |

## Fluxo

Frontend → Gateway (8080) → Eureka → Serviço de destino

## Execução

```bash
mvn clean package -DskipTests
java -jar target/gateway-service-1.0.0.jar
```

---

* `bootstrap.yml`: Carrega as configs do Config Server, que roda na porta 8888
* `application.yml`: Configuração local, fallback caso o Config Server esteja offline
* `GatewayServiceApplication.java`: Incluir anotação `@EnableDiscoveryClient`
* `Dockerfile`
* `README.md`


