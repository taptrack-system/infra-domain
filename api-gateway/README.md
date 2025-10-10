# API Gateway

* Actuator: http://julianemaran.mshome.net:8080/actuator
* Constructed eureka meta-data healthcheckUrl: http://JulianeMaran.mshome.net:8080/actuator/health
* Constructed eureka meta-data statusPageUrl: http://JulianeMaran.mshome.net:8080/actuator/info
* HTTP GET http://localhost:8761/eureka/apps/

## Estrutura do Projeto

```text
api-gateway/
├── Dockerfile
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── infradomain
    │   │           └── apigateway
    │   │               └── ApiGatewayApplication.java
    │   └── resources
    │       ├── application.yml
    │       └── logback-spring.xml
    └── test
        └── java
            └── com
                └── infradomain
                    └── apigateway
                        └── ApiGatewayApplicationTests.java
```

---

## Rotas

* O Gateway usa service discovery (`lb://service-name`) para rotear requisições automaticamente.
* Por exemplo, `/api/v1/auth/**` será roteado para o microsserviço `auth-service`.
* O Discovery Locator habilita roteamento automático baseado no `spring.application.name` de cada microsserviço.

---

## Executando a Aplicação

### Execução Local

```bash
# Build
mvn clean package -DskipTests

# Executar
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
```

Acesse:

* Eureka: http://localhost:8761
* Gateway: http://localhost:8080

### Execução Via Docker

```bash
docker build -t api-gateway .
docker run -d -p 8080:8080 --name gateway api-gateway
```

## Testes com Eureka e Microsserviços

Quando subir tudo junto no `docker-compose`, o Gateway vai registrar-se no Eureka e automaticamente rotear as
requisições para os microsserviços `auth-service` e `users-service`.

> Exemplo:
> `http://localhost:8080/api/v1/auth/health` → encaminha para `auth-service`
> `http://localhost:8080/api/v1/users/health` → encaminha para `users-service`

---

## Resumo

| Serviço           | Porta | Função                      | URL                                            |
|-------------------|-------|-----------------------------|------------------------------------------------|
| **Eureka Server** | 8761  | Registro de microsserviços  | [http://localhost:8761](http://localhost:8761) |
| **API Gateway**   | 8080  | Proxy + Roteamento dinâmico | [http://localhost:8080](http://localhost:8080) |

---

## GUIA DE CONFIGURAÇÃO — EUREKA CLIENT (ex.: API Gateway)

Todo cliente (Gateway, Auth, User, etc.) deve:

* Registrar-se no Eureka Server.
* Enviar _heartbeats_ periódicos.
* Renovar a lease antes do tempo de expiração.
* Descobrir outros serviços quando necessário.

Essas operações são controladas pelas chaves dentro de `eureka.client` e `eureka.instance`.

### PRINCIPAIS PARÂMETROS

| Categoria    | Parâmetro                                              | Descrição                                    |
|--------------|--------------------------------------------------------|----------------------------------------------|
| **Client**   | `eureka.client.service-url.defaultZone`                | Endereço do(s) servidor(es) Eureka           |
|              | `eureka.client.register-with-eureka`                   | Define se este serviço deve se registrar     |
|              | `eureka.client.fetch-registry`                         | Define se deve buscar lista de serviços      |
| **Instance** | `eureka.instance.lease-renewal-interval-in-seconds`    | Intervalo entre heartbeats enviados          |
|              | `eureka.instance.lease-expiration-duration-in-seconds` | Tempo máximo antes de ser considerado “DOWN” |
|              | `eureka.instance.prefer-ip-address`                    | Usa IP em vez de hostname no registro        |
|              | `eureka.instance.hostname`                             | Nome visível no dashboard do Eureka          |
|              | `eureka.instance.instance-id`                          | ID único por instância (evita conflitos)     |

### 1. Ambiente LOCAL

**Cenário:** rodando tudo no notebook (Eureka + Gateway + outros microsserviços).

* **Explicação do `application-local.yml`:**
    * Heartbeat rápido (10s) para testar comportamento dinâmico.
    * Expiração curta (30s) facilita visualizar UP/DOWN no dashboard.
    * `instance-id` usa valor randômico — ideal em ambiente local com vários containers.

* **Explicação do `application-local.yml`:**

### 2. Ambiente HOMOLOGAÇÃO / STAGE

**Cenário:** microsserviços rodando em containers ou pods, com Eureka centralizado.

* **Explicação do `application-hom.yml`:**
    * `30s/90s` é o padrão de referência (relação 1:3).
    * Balanceia tempo de atualização e estabilidade.
    * Ideal para clusters de homologação, com reinícios frequentes.

---

| Warning / Log                                       | Causa                   | Gravidade      | Solução                           |
|-----------------------------------------------------|-------------------------|----------------|-----------------------------------|
| `LoadBalancer default cache`                        | Cache padrão em uso     | ⚪ Baixa        | Adicionar Caffeine + CacheManager |
| `Unsafe::allocateMemory` (Netty)                    | Acesso nativo à memória | ⚪ Baixa        | Atualizar Netty (ou ignorar)      |
| `Java agent loaded dynamically`                     | ByteBuddy / Mockito     | ⚪ Baixa        | Ignorar em dev/test               |
| `Unsafe::staticFieldBase` (Guice)                   | Acesso AOP interno      | ⚪ Baixa        | Atualizar Guice (ou ignorar)      |
| `Sharing is only supported for boot loader classes` | CDS do JVM              | ⚪ Baixa        | Ignorar                           |
| `Eureka HTTP Client uses RestTemplate`              | Log normal              | 🟢 Informativo | Nenhuma ação necessária           |
