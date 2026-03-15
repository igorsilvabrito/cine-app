# 🎬 Cine App

> Sistema web de gerenciamento e compra de ingressos de cinema, com seleção de filmes, sessões, salas e assentos — incluindo controle de concorrência e disponibilidade em tempo real.

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-Cache-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)

---

## 📋 Sumário

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Diagrama de Entidades (ERD)](#-diagrama-de-entidades-erd)
- [Fluxo de Compra de Ingresso](#-fluxo-de-compra-de-ingresso)
- [Endpoints da API](#-endpoints-da-api)
- [Autenticação e Perfis](#-autenticação-e-perfis)
- [Como Executar](#-como-executar)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Configuração](#%EF%B8%8F-configuração)
- [Controle de Concorrência com Redis](#-controle-de-concorrência-com-redis)
- [Migrações do Banco de Dados](#-migrações-do-banco-de-dados)
- [Contribuindo](#-contribuindo)
- [Autor](#-autor)

---

## 📖 Sobre o Projeto

O **Cine App** é uma API REST desenvolvida em Java com Spring Boot para gerenciar a operação completa de um cinema. O sistema permite que usuários naveguem por filmes, sessões e salas, reservem e comprem ingressos com controle de concorrência via Redis, garantindo que dois usuários não consigam comprar o mesmo assento ao mesmo tempo.

---

## ✨ Funcionalidades

- 🎥 Cadastro e listagem de filmes (soft delete com flag `active`)
- 🏛️ Criação de salas com geração automática de assentos (ex: A1, A2, B1...)
- 🕐 Gerenciamento de sessões vinculadas a filme e sala, com preço e horário
- 💺 Listagem de assentos por sessão com status em tempo real (`AVAILABLE`, `HELD`, `RESERVED`)
- 🔒 Sistema de **hold de assento via Redis** com expiração de 5 minutos
- 🎫 Compra de ingresso apenas para assentos reservados pelo próprio usuário
- 👥 Controle de usuários com perfis hierárquicos (`ADMIN > MANAGER > ATTENDANT > CUSTOMER`)
- 🔐 Autenticação JWT stateless
- 🗄️ Migrações de banco de dados versionadas com Flyway

---

## 🛠️ Tecnologias

| Tecnologia | Versão | Descrição |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 3.5.9 | Framework principal |
| Spring Security | — | Autenticação e autorização JWT |
| Spring Data JPA | — | Persistência ORM com Hibernate |
| Spring Data Redis | — | Cache e controle de hold de assentos |
| Flyway | — | Migrações versionadas do banco de dados |
| MySQL | 8.0 | Banco de dados relacional |
| Redis | — | Cache para reserva temporária de assentos |
| Lombok | 1.18.30 | Redução de boilerplate |
| Auth0 Java JWT | 4.5.0 | Geração e validação de tokens JWT |
| Maven | 3.8+ | Gerenciamento de dependências e build |

---

## 🗂️ Diagrama de Entidades (ERD)

```
┌──────────────────┐         ┌──────────────────────┐         ┌──────────────────┐
│      USERS       │         │      USERS_PROFILES   │         │    PROFILES      │
├──────────────────┤         ├──────────────────────┤         ├──────────────────┤
│ id (PK)          │────────►│ user_id (FK)         │◄────────│ id (PK)          │
│ name             │         │ profile_id (FK)       │         │ name (ENUM)      │
│ email (UNIQUE)   │         └──────────────────────┘         │  ADMIN           │
│ password         │                                           │  MANAGER         │
│ active           │                                           │  ATTENDANT       │
└──────────────────┘                                           │  CUSTOMER        │
        │                                                      └──────────────────┘
        │ 1
        │
        ▼ N
┌──────────────────┐
│     TICKETS      │
├──────────────────┤         ┌──────────────────┐         ┌──────────────────┐
│ id (PK)          │         │     SESSIONS     │         │      MOVIES      │
│ user_id (FK)     │         ├──────────────────┤         ├──────────────────┤
│ session_id (FK)  │────────►│ id (PK)          │────────►│ id (PK)          │
│ session_seat_id  │         │ movie_id (FK)    │         │ title            │
│   (FK, UNIQUE)   │         │ room_id (FK)     │         │ description      │
│ price            │         │ start_time       │         │ duration_minutes │
│ purchased_at     │         │ price            │         │ rating           │
└──────────────────┘         │ active           │         │ active           │
                             └──────────────────┘         └──────────────────┘
                                      │
                                      │ 1
                                      │
                                      ▼ N
                             ┌──────────────────┐         ┌──────────────────┐
                             │  SESSION_SEATS   │         │      ROOMS       │
                             ├──────────────────┤         ├──────────────────┤
                             │ id (PK)          │         │ id (PK)          │
                             │ session_id (FK)  │         │ name             │
                             │ seat_id (FK)     │         │ total_rows       │
                             │ status (ENUM)    │         │ total_columns    │
                             │  AVAILABLE       │         │ active           │
                             │  HELD            │         └──────────────────┘
                             │  RESERVED        │                  │ 1
                             └──────────────────┘                  │
                                      │                            ▼ N
                                      │                   ┌──────────────────┐
                                      │                   │      SEATS       │
                                      └──────────────────►│ id (PK)          │
                                                          │ code (A1, B2...) │
                                                          │ room_id (FK)     │
                                                          └──────────────────┘
```

---

## 🔄 Fluxo de Compra de Ingresso

O sistema segue um fluxo de 3 etapas para garantir consistência e evitar conflitos:

```
1. HOLD (Redis - 5 min)        2. RESERVE (banco)          3. PURCHASE (ingresso)
─────────────────────          ──────────────────          ──────────────────────
POST /sessions/{id}            PATCH /sessions/{id}        POST /sessions/{id}
     /seats/{id}/hold               /seats/{id}/reserve         /seats/{id}/tickets
          │                              │                            │
          ▼                              ▼                            ▼
  Redis guarda userId           Status → RESERVED            Ticket criado no BD
  key: seat:{s}:{seat}          (valida hold no Redis)        Hold liberado no Redis
  TTL: 5 minutos                (valida dono do hold)         (valida RESERVED + dono)

  → 409 se já em hold           → erro se não há hold         → erro se não RESERVED
                                → erro se outro usuário        → erro se outro usuário
```

---

## 🔌 Endpoints da API

> **Base URL:** `http://localhost:8080`
> Todos os endpoints (exceto `/auth/login` e `/auth/register`) requerem o header:
> `Authorization: Bearer <token>`

---

### 🔐 Autenticação — `/auth`

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|:---:|-----------|
| `POST` | `/auth/login` | ❌ | Realiza login e retorna JWT |
| `POST` | `/auth/register` | ❌ | Registra novo usuário (perfil CUSTOMER) |
| `GET` | `/auth/users` | ✅ | Lista todos os usuários |
| `PATCH` | `/auth/add-profile/{id}` | ✅ | Adiciona perfil a um usuário |
| `PUT` | `/auth/{id}/deactivate` | ✅ | Desativa um usuário |
| `PUT` | `/auth/{id}/active` | ✅ | Ativa um usuário |

**Login — body:**
```json
{
  "email": "usuario@email.com",
  "password": "senha123"
}
```
**Login — resposta:**
```json
{
  "name": "João Silva",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Register — body:**
```json
{
  "name": "João Silva",
  "email": "joao@email.com",
  "password": "senha123"
}
```

**Add Profile — body:**
```json
{
  "profileName": "MANAGER"
}
```

---

### 🎥 Filmes — `/movies`

| Método | Endpoint | Perfil mínimo | Descrição |
|--------|----------|:---:|-----------|
| `GET` | `/movies` | Qualquer autenticado | Lista filmes ativos |
| `POST` | `/movies` | `ADMIN` | Cadastra novo filme |
| `PUT` | `/movies/{id}` | `ADMIN` | Atualiza dados do filme |
| `DELETE` | `/movies/{id}` | `ADMIN` | Desativa filme (soft delete) |

**Body (POST / PUT):**
```json
{
  "title": "Inception",
  "description": "Um ladrão que rouba segredos corporativos...",
  "durationMinutes": 148,
  "rating": "12"
}
```

**Resposta:**
```json
{
  "id": 1,
  "title": "Inception",
  "description": "Um ladrão que rouba segredos corporativos...",
  "durationMinutes": 148,
  "rating": "12"
}
```

---

### 🏛️ Salas — `/rooms`

> Ao criar uma sala, os assentos são gerados automaticamente com códigos como `A1`, `A2`, `B1`...

| Método | Endpoint | Perfil mínimo | Descrição |
|--------|----------|:---:|-----------|
| `GET` | `/rooms` | Qualquer autenticado | Lista salas ativas |
| `POST` | `/rooms` | `ADMIN` | Cria sala e gera assentos automaticamente |

**Body (POST):**
```json
{
  "name": "Sala IMAX",
  "rows": 10,
  "columns": 12
}
```

**Resposta:**
```json
{
  "id": 1,
  "name": "Sala IMAX",
  "rows": 10,
  "columns": 12
}
```

---

### 🕐 Sessões — `/sessions`

> Ao criar uma sessão, os `session_seats` são gerados automaticamente para todos os assentos da sala com status `AVAILABLE`. Não é possível criar duas sessões na mesma sala no mesmo horário.

| Método | Endpoint | Perfil mínimo | Descrição |
|--------|----------|:---:|-----------|
| `GET` | `/sessions` | Qualquer autenticado | Lista sessões ativas |
| `POST` | `/sessions` | `ADMIN` | Cria sessão (gera session_seats automaticamente) |

**Body (POST):**
```json
{
  "movieId": 1,
  "roomId": 2,
  "startTime": "2025-06-15T19:00:00",
  "price": 35.00
}
```

**Resposta:**
```json
{
  "id": 1,
  "movieTitle": "Inception",
  "roomName": "Sala IMAX",
  "startTime": "2025-06-15T19:00:00",
  "price": 35.00
}
```

---

### 💺 Assentos da Sessão — `/sessions/{sessionId}/seats`

| Método | Endpoint | Perfil mínimo | Descrição |
|--------|----------|:---:|-----------|
| `GET` | `/sessions/{sessionId}/seats` | Público | Lista assentos e status da sessão |
| `POST` | `/sessions/{sessionId}/seats/{seatId}/hold` | Autenticado | Reserva temporária via Redis (5 min) |
| `PATCH` | `/sessions/{sessionId}/seats/{seatId}/reserve` | Autenticado | Confirma reserva no banco (requer hold ativo) |

**GET — resposta:**
```json
[
  { "id": 1, "seatCode": "A1", "status": "AVAILABLE" },
  { "id": 2, "seatCode": "A2", "status": "HELD" },
  { "id": 3, "seatCode": "A3", "status": "RESERVED" }
]
```

**Hold — resposta:** `204 No Content` (ou `409 Conflict` se já estiver em hold)

**Reserve — resposta:** `204 No Content`

---

### 🎫 Ingressos — `/sessions/{sessionId}/seats/{seatId}/tickets`

| Método | Endpoint | Perfil mínimo | Descrição |
|--------|----------|:---:|-----------|
| `POST` | `/sessions/{sessionId}/seats/{seatId}/tickets` | Autenticado | Compra o ingresso (requer assento RESERVED pelo próprio usuário) |

**Resposta:** `204 No Content`

Ao comprar, o sistema:
1. Valida que o assento está em `hold` no Redis pelo usuário autenticado
2. Valida que o status no banco é `RESERVED`
3. Cria o `Ticket` com preço da sessão e timestamp
4. Libera o `hold` no Redis

---

## 🔑 Autenticação e Perfis

A autenticação é feita via **JWT (Bearer Token)** com expiração de **30 minutos**. O token é gerado com a biblioteca `auth0/java-jwt`.

### Hierarquia de Perfis

```
ADMIN
  └── MANAGER
        └── ATTENDANT
              └── CUSTOMER
```

Perfis superiores herdam todas as permissões dos inferiores. Um `ADMIN` pode fazer tudo que `MANAGER`, `ATTENDANT` e `CUSTOMER` fazem.

| Perfil | Permissões |
|---|---|
| `ADMIN` | Acesso total (filmes, salas, sessões, usuários) |
| `MANAGER` | Gerenciamento de sessões e usuários |
| `ATTENDANT` | Operações de atendimento |
| `CUSTOMER` | Listar, fazer hold, reservar e comprar ingressos |

### Configuração do Admin padrão

O admin padrão é configurado via variáveis de ambiente (com fallback):

```properties
admin.email=${ADMIN_EMAIL:admin@email.com}
admin.password=${ADMIN_PASSWORD:admin123}
admin.name=${ADMIN_NAME:Administrador}
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.8+
- MySQL 8.0 rodando na porta `3306`
- Redis rodando na porta `6379`

### 1. Clone o repositório

```bash
git clone https://github.com/igorsilvabrito/cine-app.git
cd cine-app
```

### 2. Configure o banco de dados

Crie o banco no MySQL:
```sql
CREATE DATABASE cine_app;
```

### 3. Configure as variáveis de ambiente (opcional)

```bash
export ADMIN_EMAIL=admin@suaempresa.com
export ADMIN_PASSWORD=senhaSegura123
export ADMIN_NAME=Administrador
```

### 4. Ajuste o `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cine_app?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
api.security.token.secret=SEU_SECRET_JWT
```

### 5. Execute a aplicação

```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

### 6. Ou gere o JAR

```bash
./mvnw clean package
java -jar target/cine-app-0.0.1-SNAPSHOT.jar
```

A API estará disponível em: **`http://localhost:8080`**

> O Flyway executará automaticamente todas as migrações SQL ao iniciar a aplicação.

---

## 📁 Estrutura do Projeto

```
cine-app/
├── src/
│   ├── main/
│   │   ├── java/com/app/cine/
│   │   │   ├── CineApplication.java           # Ponto de entrada
│   │   │   ├── controller/                    # Endpoints REST
│   │   │   │   ├── AuthController.java        # Login
│   │   │   │   ├── UserController.java        # Usuários
│   │   │   │   ├── MovieController.java       # Filmes
│   │   │   │   ├── RoomController.java        # Salas
│   │   │   │   ├── SessionController.java     # Sessões
│   │   │   │   ├── SessionSeatController.java # Assentos (hold/reserve)
│   │   │   │   └── TicketController.java      # Compra de ingresso
│   │   │   ├── service/                       # Regras de negócio
│   │   │   │   ├── SeatHoldService.java       # Hold via Redis
│   │   │   │   ├── SessionSeatService.java    # Lógica de reserva
│   │   │   │   ├── TicketService.java         # Lógica de compra
│   │   │   │   └── ...
│   │   │   ├── entity/                        # Entidades JPA
│   │   │   │   ├── movies/Movie.java
│   │   │   │   ├── room/Room.java
│   │   │   │   ├── seats/Seat.java
│   │   │   │   ├── session/{Session, SessionSeat, SeatStatus}.java
│   │   │   │   ├── ticket/Ticket.java
│   │   │   │   └── user/{User, Profile, ProfileName}.java
│   │   │   ├── dto/                           # Records de request/response
│   │   │   ├── repository/                    # Interfaces Spring Data JPA
│   │   │   ├── mapper/                        # Conversão entidade ↔ DTO
│   │   │   └── infra/security/               # JWT, filtros, Redis, CORS
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/                 # Scripts Flyway (V1 a V9)
│   └── test/
├── pom.xml
└── mvnw / mvnw.cmd
```

---

## ⚙️ Configuração

```properties
# Banco de dados
spring.datasource.url=jdbc:mysql://localhost:3306/cine_app?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

# JWT
api.security.token.secret=minhaSenha

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Flyway
spring.flyway.enabled=true

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# Admin padrão
admin.email=${ADMIN_EMAIL:admin@email.com}
admin.password=${ADMIN_PASSWORD:admin123}
admin.name=${ADMIN_NAME:Administrador}
```

> ⚠️ Em produção, substitua `api.security.token.secret` por uma chave forte e utilize variáveis de ambiente para credenciais sensíveis.

---

## 🔒 Controle de Concorrência com Redis

O sistema usa **Redis** para gerenciar a reserva temporária de assentos e evitar conflitos em situações de alta concorrência.

### Como funciona o Hold

```java
// Chave Redis: "seat:{sessionId}:{seatId}"
// Valor: userId (dono do hold)
// TTL: 5 minutos

redisTemplate.opsForValue()
    .setIfAbsent(key, userId.toString(), 5, TimeUnit.MINUTES);
```

O comando `setIfAbsent` (equivalente ao `SET NX` do Redis) garante atomicidade: apenas o primeiro usuário a chamar o endpoint consegue o hold.

### Fluxo completo de validações

```
POST /hold
  └── setIfAbsent no Redis → retorna false se já ocupado → 409 Conflict

PATCH /reserve
  └── isHeld? → não → lança exceção "Assento não está em hold"
  └── getHolder == userId? → não → lança exceção "Pertence a outro usuário"
  └── status == AVAILABLE? → não → lança exceção "Cadeira indisponível"
  └── setStatus(RESERVED) ✅

POST /tickets
  └── isHeld? → não → lança exceção "Assento não está em hold"
  └── getHolder == userId? → não → lança exceção "Assento pertence a outro usuário"
  └── status == RESERVED? → não → lança exceção "Assento não está reservado"
  └── cria Ticket + releaseSeat no Redis ✅
```

---

## 🗄️ Migrações do Banco de Dados

O projeto usa **Flyway** para versionamento do banco. As migrações são executadas automaticamente na inicialização:

| Versão | Arquivo | Descrição |
|--------|---------|-----------|
| V1 | `V1__create-table-users.sql` | Tabela de usuários |
| V2 | `V2__create-table-profiles.sql` | Tabela de perfis + inserts iniciais |
| V3 | `V3__create_users_profiles.sql` | Tabela de relacionamento usuário-perfil |
| V4 | `V4__create-table-movies.sql` | Tabela de filmes |
| V5 | `V5__create-table-rooms.sql` | Tabela de salas |
| V6 | `V6__create-table-seats.sql` | Tabela de assentos |
| V7 | `V7__create-table-sessions.sql` | Tabela de sessões (constraint UNIQUE sala+horário) |
| V8 | `V8__create-table-session-seats.sql` | Tabela de assentos por sessão |
| V9 | `V9__create-table-tickets.sql` | Tabela de ingressos (seat UNIQUE por sessão) |

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do projeto
2. Crie uma branch para sua feature:
   ```bash
   git checkout -b feature/minha-feature
   ```
3. Faça commit das suas mudanças:
   ```bash
   git commit -m 'feat: adiciona minha feature'
   ```
4. Faça push para a branch:
   ```bash
   git push origin feature/minha-feature
   ```
5. Abra um Pull Request

---

## 👤 Autor

**Igor Silva Brito**

- GitHub: [@igorsilvabrito](https://github.com/igorsilvabrito)

---
