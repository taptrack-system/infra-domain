# Gateway Service

O `gateway-service` é um **API Gateway reativo** construído com **Spring Cloud Gateway** e **Spring WebFlux**. Ele atua
como ponto de entrada único para todos os serviços do sistema, oferecendo:

- Roteamento dinâmico de requisições via **Eureka**
- Filtros globais para logging ou autenticação
- Observabilidade completa via **Spring Boot Actuator**
- Suporte a programação reativa com **Project Reactor**
- Configuração de logging detalhado para monitoramento de rotas

---

## Tecnologias e Dependências

- **Java:** 25
- **Spring Boot:** 3.5.6
- **Spring Cloud Gateway:** 2025.0.0
- **Spring WebFlux:** programação reativa
- **Spring Cloud Netflix Eureka Client:** registro e descoberta de serviços
- **Spring Boot Actuator:** endpoints de health, métricas e logs
- **Testes:** Spring Boot Test e Reactor Test

---

## Estrutura do Projeto

```
gateway-service/
 ├─ src/main/java/com/infradomain/gatewayservice/
 │   ├─ GatewayServiceApplication.java
 │   └─ config/
 │       ├─ GatewayConfig.java
 │       └─ LoggingFilter.java
 └─ src/main/resources/
     └─ application.yml
```

### Observações

* Rotas do Gateway (`GatewayConfig.java`)
    * `lb://service-a` e `lb://service-b` indicam **Load Balancing via Eureka**

* Filtro Global de Logging (`LoggingFilter.java`)
    * Log simples das requisições que passam pelo Gateway
    * Mantém compatibilidade reativa (`Mono<void>`)

---

## Como Rodar

1. Clone do repositório
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd gateway-service
   ```
2. Build do projeto
   ```bash
   mvn clean install

   ```
3. Rodar o Gateway
   ```bash
   mvn spring-boot:run
   ```

* Acessível em: `http://localhost:8080`
* Actuator:
    * `http://localhost:8080/actuator/health`
    * `http://localhost:8080/actuator/metrics`

---

## Próximos Passos

1. Adicionar autenticação JWT ou OAuth2 nos filtros globais.
2. Configurar **rate limiting** para controle de tráfego
3. Integrar com **Prometheus/Grafana** para métricas avançadas
4. Criar filtros customizados para logging estruturado ou auditoria