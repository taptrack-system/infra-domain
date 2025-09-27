# Discovery Service

O `discovery-service` é o **Service Discovery** do ecossistema de microsserviços. Ele utiliza **Spring Cloud Netflix
Eureka** para:

- Registro automático de serviços
- Descoberta de serviços para outros microsserviços (ex: Gateway, Service A/B/C)
- Monitoramento via Actuator

---

## Tecnologias e Dependências

- **Java:** 25
- **Spring Boot:** 3.5.6
- **Spring Cloud Netflix Eureka Server:** Service Discovery
- **Spring Boot Actuator:** health e métricas

## Estrutura do Projeto

```
discovery-service/
 ├─ src/main/java/com/infra_domain/discovery/
 │   └─ DiscoveryServiceApplication.java
 └─ src/main/resources/
     └─ application.yml
```

## Diagrama Arquitetural (Mermaid)

```mermaid
graph TD
    subgraph Eureka Server
        DS[Discovery Service<br>Eureka Server]
    end

    subgraph Registered Services
        GW[Gateway Service]
        A[Service A]
        B[Service B]
        C[Service C]
    end

%% Conexões
    GW -->|Registro/Descoberta| DS
    A -->|Registro/Descoberta| DS
    B -->|Registro/Descoberta| DS
    C -->|Registro/Descoberta| DS
%% Comunicação indireta via Eureka
    GW -->|Roteamento dinâmico| A
    GW -->|Roteamento dinâmico| B
    GW -->|Roteamento dinâmico| C
%% Observabilidade
    DS -->|Actuator endpoints| ACT[Actuator /actuator]
````

**Explicações do diagrama:**

* `DS` → Eureka Server (`discovery-service`)
* `GW`, `A`, `B`, `C` → microsserviços registrados no Eureka
* Todos os serviços se registram no `discovery-service` e podem descobrir uns aos outros
* `Gateway` usa Eureka para roteamento dinâmico
* Actuator fornece endpoints de health e métricas do Eureka Server

## Como Rodar

1. Clone do repositório
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd discovery-service
   ```
2. Build do projeto
   ```bash
   mvn clean install
   ```
3. Rodar o Discovery Service
   ```bash
   mvn spring-boot:run
   ```

* Acessível em: `http://localhost:8761`
* Interface do Eureka: `http://localhost:8761`
* Actuator:
    * `http://localhost:8761/actuator/health`
    * `http://localhost:8761/actuator/metrics`

---

## Observações

* Serviços clientes (ex: `gateway-service`, `service-a`) devem configurar
  `eureka.client.service-url.defaultZone=http://localhost:8761/eureka/`.
* Eureka Server mantém registro de todos os microsserviços ativos e fornece descoberta automática para o Gateway e
  outros serviços.
* Para desenvolvimento local, desative a auto-preservação (`enable-self-preservation=false`) para refletir
  instantaneamente serviços registrados.

---

## Próximos Passos

* Registrar o `gateway-service` e outros microsserviços no Eureka
* Integrar com rotas dinâmicas no Gateway usando os nomes dos serviços registrados
* Monitorar métricas e health checks dos serviços via Actuator
