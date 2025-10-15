# Config Server

## Execução

### Via Docker

```bash
docker compose up --build
```

* **Visualizar Logs**

```bash
docker logs config-server
docker logs eureka-server
docker logs api-gateway
```

* **Ver logs em tempo real (modo "follow")**

```bash
docker logs -f config-server
```

* **Ver logs recentes (limitando linhas)**

Se quiser limitar a quantidade de linhas (ex: últimas 50):

```bash
docker logs --tail 50 config-server
```

* **Se estiver usando Docker Compose**

Pode visualizar os logs de todos os serviços juntos:

```bash
docker compose logs
```

Ou de um serviço específico:

```bash
docker compose logs config-server
```

E para acompanhar em tempo real:

```bash
docker compose logs -f config-server
```

```bash
```

```bash
```

```bash
```