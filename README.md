# 🎬 Cine App

> Sistema web de gerenciamento e compra de ingressos de cinema, com seleção de filmes, sessões, salas e assentos — incluindo controle de concorrência e disponibilidade em tempo real.

---

## 📋 Sobre o Projeto

O **Cine App** é uma aplicação back-end desenvolvida em Java com Spring Boot para gerenciar a operação completa de um cinema. O sistema permite que usuários escolham filmes, sessões, salas e assentos, garantindo consistência na disponibilidade de ingressos mesmo em cenários de alta concorrência.

---

## ✨ Funcionalidades

- 🎥 Cadastro e listagem de filmes
- 🕐 Gerenciamento de sessões por sala e horário
- 🏛️ Controle de salas e capacidade
- 💺 Seleção e reserva de assentos
- 🔒 Controle de concorrência para evitar dupla venda
- ✅ Verificação de disponibilidade em tempo real

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Descrição |
|---|---|
| Java | Linguagem principal (100%) |
| Spring Boot | Framework web e injeção de dependências |
| Maven | Gerenciamento de dependências e build |
| Spring Data JPA | Persistência de dados |
| Banco de dados relacional | Armazenamento de filmes, sessões e reservas |

---

## 🚀 Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.8+

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/igorsilvabrito/cine-app.git
   cd cine-app
   ```

2. Execute com o Maven Wrapper:
   ```bash
   # Linux/macOS
   ./mvnw spring-boot:run

   # Windows
   mvnw.cmd spring-boot:run
   ```

3. Ou gere o JAR e execute:
   ```bash
   ./mvnw clean package
   java -jar target/cine-app-*.jar
   ```

4. A API estará disponível em:
   ```
   http://localhost:8080
   ```

---

## 📁 Estrutura do Projeto

```
cine-app/
├── .mvn/wrapper/          # Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/          # Código-fonte principal
│   │   └── resources/     # Configurações (application.properties)
│   └── test/              # Testes automatizados
├── pom.xml                # Dependências e configurações Maven
└── mvnw / mvnw.cmd        # Scripts do Maven Wrapper
```

---

## ⚙️ Configuração

As configurações da aplicação (banco de dados, porta, etc.) estão em:

```
src/main/resources/application.properties
```

Exemplo de configuração para banco H2 em memória (desenvolvimento):
```properties
spring.datasource.url=jdbc:h2:mem:cinedb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
```

---

## 🔄 Controle de Concorrência

Um dos principais desafios do sistema é garantir que dois usuários não consigam reservar o mesmo assento simultaneamente. O Cine App trata esse cenário utilizando mecanismos de controle de concorrência (como bloqueio otimista ou pessimista via JPA), assegurando a integridade dos dados em situações de alta demanda.

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/minha-feature`)
3. Commit suas mudanças (`git commit -m 'feat: adiciona minha feature'`)
4. Push para a branch (`git push origin feature/minha-feature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está sob licença open source. Consulte o repositório para mais detalhes.

---

## 👤 Autor

**Igor Silva Brito**

- GitHub: [@igorsilvabrito](https://github.com/igorsilvabrito)
