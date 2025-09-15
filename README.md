# agent-backend
Repositório criado para controle do Agent-Backend

# 🐳 Guia de Execução do Backend com Docker

Este documento explica como rodar o backend do projeto utilizando Docker e Docker Compose.

---

## 1. Pré-requisitos

Antes de iniciar, certifique-se de ter instalado:

- [Docker](https://www.docker.com/get-started) (versão 20+)
- [Docker Compose](https://docs.docker.com/compose/install/) (versão 1.29+)
- [Java 21 JDK](https://adoptium.net/) (opcional, apenas para rodar local sem Docker)
- [Maven 3.9+](https://maven.apache.org/) (opcional, apenas para rodar local sem Docker)

---

## 2. Estrutura do Backend

O backend está organizado seguindo o padrão Spring Boot, com pacotes separados por responsabilidade.

### Estrutura principal do projeto

```text
backend-agent [agent-sender]
│
├─ src
│  ├─ main
│  │  ├─ java
│  │  │  └─ com.dev.agent
│  │  │     ├─ config                   # Configurações da aplicação (CORS, beans, segurança, etc.)
│  │  │     ├─ controller               # Controladores REST que expõem os endpoints da API
│  │  │     ├─ dto                      # Objetos de transferência de dados (Data Transfer Objects)
│  │  │     ├─ entity                   # Entidades do banco de dados
│  │  │     ├─ enums                    # Enumerações utilizadas na aplicação
│  │  │     ├─ repository               # Repositórios Spring Data para acesso ao banco
│  │  │     ├─ services                 # Lógica de negócio da aplicação
│  │  │     └─ AgentApplication.java    # Classe principal que inicializa a aplicação
│  │  └─ resources
│  │     └─ application.yml             # Arquivo de configuração do Spring Boot
│  │
│  └─ test
│     └─ java                           # Testes unitários
│
├─ docker-compose.yml                   # Configuração dos containers Docker
├─ Dockerfile                           # Dockerfile para criar a imagem do backend
├─ HELP.md                              # Documentação auxiliar
├─ mvnw / mvnw.cmd                      # Wrapper do Maven
└─ pom.xml                              # Arquivo de configuração do Maven
```

---

## 3. Build da Imagem Docker

No diretório raiz do backend, execute:
```bash
docker build -t agent-backend:latest .
```

### 3.1 Explicação do Dockerfile:

#### BUILD STAGE
* Base: maven:3.9.9-eclipse-temurin-21
* Baixa dependências e cria o jar executável.
* Comando: mvn clean package spring-boot:repackage -DskipTests
* Produz o target/app.jar.
#### RUN TIME STAGE
* Base: eclipse-temurin:21-jre
* Copia o jar do build.
* Define variável JAVA_OPTS para memória (-Xms256m -Xmx512m).
* Expõe porta 8080.
* Executa: java $JAVA_OPTS -jar app.jar.

---

## 4. Rodando com Docker Compose

* Para iniciar todos os serviços definidos no docker-compose.yml:
```bash
docker compose up -d --build
```

* Para parar os serviços:
```bash
docker compose down
```

---

## 5. Variáveis de Ambiente

As variáveis de ambiente configuram o comportamento da aplicação e dos serviços dependentes.

### Backend (`agent-backend`)


| Variável                  | Valor/Exemplo                       | Descrição                                         |
|----------------------------|-------------------------------------|--------------------------------------------------|
| `FRONTEND_URL`             | `http://localhost:4200`             | URL do frontend que o backend irá acessar       |
| `SPRING_DATASOURCE_URL`    | `jdbc:postgresql://postgres:5432/nome_do_banco` | URL de conexão com o banco PostgreSQL           |
| `SPRING_DATASOURCE_USERNAME` | `usuario_do_banco`                | Usuário do banco de dados                        |
| `SPRING_DATASOURCE_PASSWORD` | `senha_do_banco`                  | Senha do banco de dados                          |
| `RABBITMQ_DEFAULT_USER`    | `usuario_rabbitmq`                  | Usuário do RabbitMQ                               |
| `RABBITMQ_DEFAULT_PASS`    | `senha_rabbitmq`                    | Senha do RabbitMQ                                 |
| `RAILWAY_TCP_PROXY_DOMAIN` | `proxy.exemplo.com`                  | Domínio do proxy do Railway (se aplicável)      |
| `RAILWAY_TCP_PROXY_PORT`   | `12345`                              | Porta do proxy do Railway                        |
| `MINIO_URL`                | `https://minio.exemplo.com:443`     | URL do serviço MinIO para armazenamento         |
| `MINIO_ACCESS_KEY`         | `chave_acesso_minio`                 | Chave de acesso MinIO                             |
| `MINIO_SECRET_KEY`         | `chave_secreta_minio`                | Chave secreta MinIO                               |

### 🔹 PostgreSQL (`postgres`)

| Variável           | Valor/Exemplo         | Descrição                       |
|-------------------|---------------------|---------------------------------|
| `POSTGRES_USER`    | `usuario_do_banco`  | Usuário do banco de dados       |
| `POSTGRES_PASSWORD`| `senha_do_banco`    | Senha do banco de dados         |
| `POSTGRES_DB`      | `nome_do_banco`     | Nome do banco a ser criado      |

---
## 6. Observações

* Certifique-se que as portas não estão em conflito.
* O backend depende do serviço PostgreSQL, definido no docker-compose.yml.