# Eureka Server

## Estrutura

```text
eureka-server/
├── Dockerfile
├── docker-compose.yml          # produção (3 nós)
├── docker-compose-hom.yml      # homolog (2 nós)
├── .env
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── infra-domain
    │   │           └── eureka-server
    │   │               └── EurekaServerApplication.java
    │   └── resources
    │       ├── application.yml
    │       └── logback-spring.xml
    └── test
        └── java
            └── com
                └── infra-domain
                    └── eureka-server
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

---

## GUIA DE AJUSTE E CONFIGURAÇÃO - SPRING CLOUD EUREKA SERVER

### Contexto

O **Eureka Server** atua como **Service Registry** centralizado.  
Cada microsserviço cliente (Gateway, Auth-Service, etc.):

* Se registra nele com informações de IP, porta, status, etc.
* **Renova periodicamente** sua lease (heartbeat).
* Pode descobrir outros serviços por nome lógico.

### Parâmetros Principais

| Categoria    | Parâmetro                                              | Descrição                                                     |
|--------------|--------------------------------------------------------|---------------------------------------------------------------|
| **Server**   | `eureka.server.enable-self-preservation`               | Evita remoção de instâncias quando há perda de heartbeats.    |
|              | `eureka.server.eviction-interval-timer-in-ms`          | Intervalo para verificar leases expiradas.                    |
|              | `eureka.server.renewal-percent-threshold`              | Percentual mínimo de renovações esperadas.                    |
| **Client**   | `eureka.client.register-with-eureka`                   | Define se o servidor deve se registrar em outro nó (cluster). |
|              | `eureka.client.fetch-registry`                         | Busca outros registros.                                       |
| **Instance** | `eureka.instance.lease-renewal-interval-in-seconds`    | Frequência dos heartbeats enviados pelos clientes.            |
|              | `eureka.instance.lease-expiration-duration-in-seconds` | Tempo limite para expirar uma instância sem heartbeat.        |
|              | `eureka.instance.prefer-ip-address`                    | Usa IP em vez de hostname.                                    |
|              | `eureka.instance.hostname`                             | Nome ou IP usado na identificação.                            |

### 1. Ambiente LOCAL (Desenvolvimento)

Cenário: 1 nó Eureka, testes locais, containers Docker.

* **Explicações (`application.yml`):**
    * `enable-self-preservation=false`: sem risco de travar o refresh de instâncias.
    * `eviction-interval-timer-in-ms=5000`: resposta rápida ao desligar serviços.
    * Útil para testes com Gateway, Auth e outros microsserviços.

### 2. Ambiente HOMOLOGAÇÃO (Stage / Testes Integrados)

Cenário: múltiplos microsserviços, ambiente controlado, instabilidade leve possível.

* **Explicações (`application-hom.yml`):**
    * `enable-self-preservation=true`: protege contra remoções falsas.
    * Intervalo de 10s é estável sem gerar carga excessiva.
    * `prefer-ip-address=true` evita problemas de DNS interno.

### 3. Ambiente de PRODUÇÃO (PRD)

Cenário: múltiplas zonas de disponibilidade, réplicas Eureka, tráfego real.

* **Explicações (`application-prod.yml`):**
    * `register-with-eureka=true`: necessário em clusters (cada nó conhece os outros).
    * `renewal-percent-threshold=0.85`: mais rigoroso para detecção de falhas reais.
    * `lease-expiration-duration-in-seconds=90`: tolera até 1,5 minutos de falha de heartbeat.
    * `peer-eureka-nodes-update-interval-ms=600000`: atualização da lista de nós a cada 10 min.

---

## Dicas Práticas de Operação

| Situação                                                                       | Solução                                                                                                             |
|--------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|
| **Mensagem “EMERGENCY! Eureka may be incorrectly claiming instances are UP…”** | Ative `enable-self-preservation=false` temporariamente em ambiente de teste, ou ajuste `renewal-percent-threshold`. |
| **Instâncias somem rapidamente**                                               | Aumente `lease-expiration-duration-in-seconds` (ex: 120).                                                           |
| **Dashboard mostra DOWN mesmo com serviço ativo**                              | Verifique se o cliente envia heartbeats (`lease-renewal-interval-in-seconds: 30`).                                  |
| **Múltiplos servidores Eureka não sincronizam**                                | Confirme `service-url.defaultZone` apontando para os outros nós e `register-with-eureka=true`.                      |
| **Gateway demora para enxergar novos serviços**                                | Ajuste `spring.cloud.discovery.client.simple.cache.expire-after-write=30s` (a partir do Spring Cloud 2024).         |

---

## Segurança Opcional (Produção)

Você pode proteger o dashboard com autenticação básica:

```yaml
spring:
  security:
    user:
      name: admin
      password: ${EUREKA_DASHBOARD_PASS:admin123}
```

E configurar os clientes:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://admin:${EUREKA_DASHBOARD_PASS}@eureka-server:8761/eureka/
```

---

## Monitoramento via Actuator

| Endpoint                     | Descrição                                   |
|------------------------------|---------------------------------------------|
| `/actuator/health`           | Verifica status do Eureka.                  |
| `/actuator/metrics/eureka.*` | Mostra métricas de leases, renovações, etc. |
| `/actuator/env`              | Mostra variáveis do ambiente ativo.         |

---

## Referência Rápida (para consulta)

| Ambiente | Self Preservation | Eviction Interval | Renewal Threshold | Register With | Lease Renewal | Lease Expiration |
|----------|-------------------|-------------------|-------------------|---------------|---------------|------------------|
| Local    | Não               | 5s                | 0.5               | Não           | 30s           | 60s              |
| Homolog  | Sim               | 10s               | 0.75              | Não           | 30s           | 90s              |
| Produção | Sim               | 60s               | 0.85              | Sim (cluster) | 30s           | 90s              |

---

## Sobre Warning

| Warning                               | Causa                        | Gravidade        | Ação recomendada                  |
|---------------------------------------|------------------------------|------------------|-----------------------------------|
| `sun.misc.Unsafe`                     | XStream usa método obsoleto  | Baixa            | Atualizar XStream (ou ignorar)    |
| `No Jakarta Bean Validation provider` | Falta do Hibernate Validator | Baixa            | Adicionar dependência ou ignorar  |
| `LoadBalancer default cache`          | Cache simples em uso         | Média (produção) | Adicionar dependência do Caffeine |
| `Replica size empty`                  | Sem peers configurados       | Baixa            | Ignorar (modo standalone)         |

---

## Executar Eureka Server

### Usando cada nó

Ao subir cada nó, defina via variável

```bash
# Nó 1
-Dspring.profiles.active=prod -DHOSTNAME=eureka-node1 -DHOST_IP=10.0.0.10

# Nó 2
-Dspring.profiles.active=prod -DHOSTNAME=eureka-node2 -DHOST_IP=10.0.0.11

# Nó 3
-Dspring.profiles.active=prod -DHOSTNAME=eureka-node3 -DHOST_IP=10.0.0.12
```

Assim, cada nó se identifica e se replica corretamente entre os peers.

### Via Docker - Produção

**Usando 3 nós - Ambiente de Produção**

1. Compile seu projeto localmente:
    ```bash
   mvn clean package -DskipTests
   mvn verify
    ```
2. Suba o cluster:
    ```bash
    docker compose up -d --build
    ```
3. Acesse:
    * Node 1: http://localhost:8761
    * Node 2: http://localhost:8762
    * Node 3: http://localhost:8763

Cada nó aparecerá registrado nos outros dois, e o dashboard do Eureka mostrará os peers conectados.

**Resumo**

| Nó           | URL de acesso                                  | IP interno | Status |
|--------------|------------------------------------------------|------------|--------|
| eureka-node1 | [http://localhost:8761](http://localhost:8761) | 10.0.0.10  | OK     |
| eureka-node2 | [http://localhost:8762](http://localhost:8762) | 10.0.0.11  | OK     |
| eureka-node3 | [http://localhost:8763](http://localhost:8763) | 10.0.0.12  | OK     |

**Todos os nós replicam entre si.**

### Via Docker - Homologação

1. Compile seu projeto localmente:
    ```bash
   mvn clean package -DskipTests
    ```
2. Suba o cluster:
    ```bash
    docker compose --env-file .env -f docker-compose-hom.yml up -d --build
    ```
3. Acesse:
    * Node 1: http://localhost:8761
    * Node 2: http://localhost:8762
4. Em cada dashboard, você verá **os dois peers registrados** entre si.

**Benefícios - Homologação**

| Recurso               | Homolog (2 nós) | Produção (3 nós) |
|-----------------------|-----------------|------------------|
| Replicação de peers   | Sim             | Sim              |
| Failover simulado     | Sim             | Sim              |
| Autopreservação ativa | Sim             | Sim              |
| Cache Caffeine        | Sim             | Sim              |
| Sem warnings          | OK              | OK               |

---

| Tipo de warning                      | Resolvido por                                 |
|--------------------------------------|-----------------------------------------------|
| `sun.misc.Unsafe`                    | Atualizar XStream                             |
| `Bean Validation provider not found` | Adicionar Hibernate Validator                 |
| `Default LoadBalancer cache`         | Adicionar Caffeine e configurar cache         |
| `Replica size empty`                 | Configuração dos peers nos ambientes hom/prod |
