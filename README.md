# Infrastructure Domain

Infraestrutura base do sistema **TapTrack**, responsável por prover serviços de configuração, descoberta e gateway para
os microsserviços.

---

## Estrutura do Repositório

```
infra-domain/
├─ config-service/       # Serviço de configuração centralizada (Spring Cloud Config) - porta 8888
├─ discovery-service/    # Serviço de descoberta (Eureka Server) - porta 8761
├─ gateway-service/      # API Gateway (Spring Cloud Gateway) - porta 8080
├─ docker-compose.yml    # Orquestração dos containers
└─ README.md
```

---

## Serviços

### Config Service

- Porta: **8888**
- Responsável por fornecer configurações centralizadas para os microsserviços.
- Repositório de configs: [config-repo](https://github.com/taptrack-system/config-repo)

### Discovery Service

- Porta: **8761**
- Registro e descoberta automática de microsserviços via **Eureka**.
- Painel Web: [http://localhost:8761](http://localhost:8761)

### Gateway Service

- Porta: **8080**
- Roteamento e balanceamento de carga dos microsserviços.
- Integra-se ao Discovery Service para resolução dinâmica de instâncias.

---

## Executando com Docker Compose

### 1. Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/) instalado (versão 20+)
- [Docker Compose](https://docs.docker.com/compose/) instalado (v2+)
- Maven 3.9.x e Java 25 para build local (opcional se já tiver os `.jar` gerados)

### 2. Build dos JARs

Antes de subir os containers, compile os serviços com Maven:

```bash
mvn clean package -DskipTests
````

Isso irá gerar os arquivos `target/*.jar` em cada serviço.

### 3. Subindo os containers

Para iniciar todos os serviços:

```bash
docker compose up --build -d
```

### 4. Verificando os logs

Acompanhar os logs de todos os serviços:

```bash
docker compose logs -f
```

Ou apenas de um serviço específico:

```bash
docker compose logs -f gateway-service
```

### 5. Parando os serviços

```bash
docker compose down
```

---

## Endpoints Principais

| Serviço           | Porta | URL Local                                      |
|-------------------|-------|------------------------------------------------|
| Config Service    | 8888  | [http://localhost:8888](http://localhost:8888) |
| Discovery Service | 8761  | [http://localhost:8761](http://localhost:8761) |
| Gateway Service   | 8080  | [http://localhost:8080](http://localhost:8080) |

---

## Tecnologias

* **Java 25** + **Spring Boot 3.5.6**
* **Spring Cloud 2025.0.0**
* **Spring Cloud Config Server**
* **Eureka Server (Netflix OSS)**
* **Spring Cloud Gateway**
* **Docker + Docker Compose**

---

## Licença

Este projeto é licenciado sob a [Apache License 2.0](LICENSE).

