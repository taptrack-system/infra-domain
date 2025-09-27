# Config Service

Serviço de **configuração centralizada** para todos os microsserviços do sistema.   
Construído com **Spring Boot 3.5.x** e **Spring Cloud Config**.

---

## Funcionalidades

- Centraliza as configurações de todos os serviços
- Suporte a **repositório local (arquivos)** ou **Git**
- Endpoints de saúde e monitoramento com Actuator
- Logging estruturado (Logback + SLF4J)
- Preparado para execução em **Docker**

---

## Estrutura do Projeto

```
config-service/
├─ src/
├─ Dockerfile
├─ pom.xml
├─ application.yml
└─ README.md
 ```

---

## Como Executar

### Compilar e rodar localmente

```bash
# Gerar o jar
mvn clean package -DskipTests

# Executar o jar
java -jar target/config-service-0.0.1-SNAPSHOT.jar
```

### Executar com Docker

```bash
# Criar a imagem
docker build -t config-service:latest .

# Rodar o container
docker run -p 8888:8888 config-service:latest
```

---

## Endpoints

| Endpoint                   | Description                                     |
|:---------------------------|-------------------------------------------------|
| `/actuator/health`         | Status de saúde da aplicação                    |
| `/actuator/info`           | Informações da aplicação                        |
| `/actuator/env`            | Propriedades de ambiente                        |
| `/actuator/metrics`        | Métricas de execução                            |
| `/actuator/refresh`        | Recarrega as configs                            |
| `/{application}/{profile}` | Busca configuração (ex: `/gateway-service/dev`) |

---

## Repositório de Configuração

Por padrão, o serviço procura arquivos de configuração em `./config-repo`.

Estrutura de exemplo:

```
config-repo/
├─ application.yml
├─ application-dev.yml
├─ gateway-service.yml
└─ gateway-service-dev.yml
```

Os clientes acessam as configs via:

- `http://localhost:8888/gateway-service/dev`
- `http://localhost:8888/application/default`

## Usando Git em vez de arquivos locais

Basta alterar o `application.yml` do serviço:

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/sua-org/config-repo
```

## Logs

* **Console:** padrão
  ```
  yyyy-MM-dd HH:mm:ss LEVEL [thread] logger - message
  ```
* **Arquivo:** `logs/config-service.log`

---

## Requirements

* JDK 25
* Maven 3.9.x
* Spring Boot 3.5.6
* Docker (optional)

---

## License

Este projeto está licenciado sob a [Apache License 2.0](./../LICENSE).