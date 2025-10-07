# Config Service

**Servidor Centralizado de Configurações para o Ecossistema de Microsserviços**

O **Config Service** é um **Spring Cloud Config Server** responsável por gerenciar e fornecer configurações
centralizadas e versionadas para todos os microsserviços do ecossistema **Tap Track**.

Ele lê arquivos de configuração hospedados em um repositório Git remoto (`config-repo`) e os disponibiliza para outros
serviços — como o **API Gateway**, o **Discovery Service (Eureka)** e os demais microsserviços de domínio (ex.:
Identity, Finance, Supplier, Product, entre outros).

---

[Visão Geral](#visão-geral)
[Arquitetura e Diagramas](#arquitetura-e-diagramas)
[Configuração e Fluxo de Propriedades](#configuração-e-fluxo-de-propriedades)
[Stack Tecnológica](#stack-tecnológica)
[Estrutura do Projeto](#estrutura-do-projeto)
[Execução e Configuração](#execução-e-configuração)
[Funcionamento Interno](#funcionamento-interno)
[Endpoints e Integrações](#endpoints-e-integrações)
[Boas Práticas](#boas-práticas)
[Licença](#licença)
[Autora](#autora)

---

## Visão Geral

O objetivo principal do **Config Service** é garantir **consistência, versionamento e centralização** de todas as
configurações do sistema distribuído.

Com isso:

- Reduz-se a duplicação de configurações entre serviços.
- É possível alterar propriedades de ambiente **sem precisar reconstruir os artefatos**.
- Cada microsserviço busca suas configurações externas diretamente no Git versionado via Spring Cloud Config.

---

## Arquitetura e Diagramas

### Contexto Arquitetural

```mermaid
graph TD
%% --- Título do diagrama ---
    T["<b>Arquitetura do Ecossistema de Microsserviços</b>"]:::title

%% --- Relações do sistema ---
    A[Frontend<br>Angular] --> B[API Gateway<br>8080]
    B --> C[Microsserviços Registrados no Eureka]
    C <--> D[Discovery Service<br>Eureka Server - 8761]
    C <--> E[Config Service<br>8888]
    E -->|Lê configurações| F[(Repositório Git:<br>config-repo)]

%% --- Estilos ---
    classDef title fill:#ffffff,stroke:#fff,stroke-width:0px,font-size:18px,font-weight:bold,color:#000;
    style A fill:#ffcc80,stroke:#333,stroke-width:1px,color:#000
    style B fill:#ffe082,stroke:#333,stroke-width:1px,color:#000
    style C fill:#fff59d,stroke:#333,stroke-width:1px,color:#000
    style D fill:#c5e1a5,stroke:#333,stroke-width:1px,color:#000
    style E fill:#a5d6a7,stroke:#333,stroke-width:1px,color:#000
    style F fill:#90caf9,stroke:#333,stroke-width:1px,color:#000

%% --- Layout visual ---
    T --- A
````

**Fluxo:**

1. O **Frontend** se comunica com o **API Gateway**.
2. O **Gateway** roteia as requisições para os **Microsserviços** registrados no **Eureka**.
3. Cada microsserviço, ao iniciar, consulta o **Config Service** para obter suas configurações.
4. O **Config Service** busca os arquivos YAML no repositório remoto `config-repo`.

---

## Configuração e Fluxo de Propriedades

### Sequência Cronológica de Carregamento

```mermaid
sequenceDiagram
    participant BS as bootstrap.yml
    participant CF as Config Server (Git)
    participant AP as application.yml
    participant AC as Application Context
    participant ACT as Actuator /info

    Note over BS: ① Carregado primeiro<br/>Define nome da aplicação<br/>e conexão com o Config Server
    BS->>CF: Conecta ao repositório remoto Git
    CF-->>BS: Retorna propriedades externas (YAMLs remotos)

    Note over AP: ② Carregado depois<br/>Define metadados, logs,<br/>e configurações locais
    AP->>AC: Popula propriedades do contexto da aplicação

    Note over AC: ③ Spring Application Context<br/>combina propriedades do bootstrap,<br/>do Config Server e do application.yml
    AC-->>ACT: ④ Actuator expõe /info e /health com<br/>as informações de info.app, build e environment
```

### Resumo Prático

| Tipo de configuração                          | Arquivo           | Fase de carregamento     | Função                              |
|-----------------------------------------------|-------------------|--------------------------|-------------------------------------|
| Nome da aplicação (`spring.application.name`) | `bootstrap.yml`   | Antes do contexto Spring | Identifica o serviço no ecossistema |
| Conexão com Git Config Server                 | `bootstrap.yml`   | Antes do contexto        | Permite buscar configs externas     |
| Metadados (`info.app`, `info.build`, etc.)    | `application.yml` | Após contexto            | Expostos via `/actuator/info`       |
| Logging (`logging.level`, `logging.pattern`)  | `application.yml` | Após contexto            | Define níveis e padrões de log      |
| Monitoramento (`management.endpoints`)        | `application.yml` | Após contexto            | Controla visibilidade de endpoints  |

---

## Stack Tecnológica

| Componente    | Tecnologia                 |
|---------------|----------------------------|
| Linguagem     | Java 25                    |
| Framework     | Spring Boot 3.5.6          |
| Cloud         | Spring Cloud Config Server |
| Build Tool    | Maven 3.9.x                |
| Versionamento | GitHub (config-repo)       |
| Logging       | SLF4J                      |
| Monitoramento | Spring Boot Actuator       |

---

## Estrutura do Projeto

```
config-service/
├── src/
│   └── main/
│       ├── java/com/infra_domain/configservice/
│       │   └── ConfigServiceApplication.java
│       └── resources/
│           ├── application.yml
│           └── bootstrap.yml
├── pom.xml
└── README.md
```

---

## Execução e Configuração

### 1. Pré-Requisitos

* **Java 25**
* **Maven 3.9+**
* **Git** com acesso ao `config-repo`
* *(Opcional)* Docker / Docker Compose

### 2. Configuração dos Arquivos

#### 📄 `bootstrap.yml`

Define o nome da aplicação e o contexto Spring Cloud:

```yaml
spring:
  application:
    name: config-service
```

#### `application.yml`

Configura o servidor para ler arquivos YAML do repositório Git:

```yaml
server:
  port: 8888

spring:
  cloud:
    config:
      server:
        git:
          uri: ${CONFIG_REPO_URI:file:///config-repo}
          clone-on-start: true
          search-paths: ''
          default-label: main
```

> **Dica:** Em desenvolvimento, monte a pasta local:
>
> ```bash
> CONFIG_REPO_URI=file:///path/to/config-repo
> ```
>
> Em produção, use a URL HTTPS ou SSH do repositório GitHub.

---

### 3. Executando o Serviço

#### Via Maven

```bash
mvn spring-boot:run
```

#### Via Docker

```bash
docker build -t config-service .
docker run -p 8888:8888 -v ./config-repo:/config-repo config-service
```

---

## Funcionamento Interno

O Config Service atua como um servidor de configurações centralizado, servindo arquivos `.yml` e `.properties`
versionados no Git para os microsserviços clientes.

### Dependência do Cliente

Cada serviço que deseja se conectar precisa incluir:

```xml

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

E apontar o servidor no `bootstrap.yml`:

```yaml
spring:
  cloud:
    config:
      uri: http://localhost:8888
      fail-fast: true
```

---

## Endpoints e Integrações

### Endpoints Principais

| Endpoint                       | Descrição                                                       |
|--------------------------------|-----------------------------------------------------------------|
| `GET /{application}/{profile}` | Retorna as configurações específicas de uma aplicação e perfil. |
| `GET /health`                  | Retorna o status de saúde do serviço (via Actuator).            |
| `GET /actuator/env`            | Exibe variáveis de ambiente carregadas.                         |

### Integrações com Outros Serviços

| Serviço                        | Porta  | Integração                                                            |
|--------------------------------|--------|-----------------------------------------------------------------------|
| **Discovery Service (Eureka)** | `8761` | Obtém suas configs via Config Server.                                 |
| **API Gateway**                | `8080` | Centraliza rotas e autenticação, com configs vindas do Config Server. |
| **Identity Profiles**          | `8081` | Recebe configs de banco de dados e segurança via Config Server.       |

---

## Boas Práticas

* **Não armazene senhas ou tokens** sensíveis em repositórios públicos. 
  * Utilize **Spring Cloud Vault** ou **HashiCorp Vault** para segredos.
* Use perfis (`application-dev.yml`, `application-prod.yml`) para ambientes distintos.
* Restrinja endpoints do Actuator a redes seguras.
* Centralize logs e métricas via **ELK** ou **Prometheus/Grafana**.

---

## Licença

Este projeto faz parte do ecossistema **Tap Track** e é distribuído sob a **licença interna da organização**.
Uso e distribuição externa dependem de autorização formal.

---

## Autora

**Juliane Maran**

*Backend Java Developer | Software Engineer (Spring Boot, APIs REST, Microservices)*

📧 [julianemaran@gmail.com](mailto:julianemaran@gmail.com)