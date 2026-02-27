# 🏦 FHBank

Sistema bancário desenvolvido com **Spring Boot** para estudo e prática de desenvolvimento backend em Java.

---

## 📋 Sobre o Projeto

O FHBank é uma API REST que simula as operações básicas de um banco digital, como gerenciamento de contas, clientes e transações financeiras. O projeto tem como objetivo praticar conceitos de backend com o ecossistema Spring.

---

## 🚀 Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Web (REST)**
- **Hibernate**
- **Lombok**
- **Maven**
- **H2 / PostgreSQL** (banco de dados)

---

## ⚙️ Como Rodar o Projeto

### Pré-requisitos

- Java 17 ou superior
- Maven instalado

### Passo a passo

```bash
# Clone o repositório
git clone https://github.com/FabioHelmer/FHBank.git

# Entre na pasta do projeto
cd FHBank

# Rode o projeto
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

---

## 📌 Funcionalidades

- [x] Cadastro de clientes
- [x] Criação e gerenciamento de contas bancárias
- [x] Depósito e saque
- [x] Transferência entre contas
- [x] Histórico de transações (com saldo anterior e posterior)

---

## 🗂️ Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/fhbank/
│   │       ├── controller/    # Endpoints REST
│   │       ├── service/       # Regras de negócio
│   │       ├── repository/    # Acesso ao banco de dados
│   │       ├── model/         # Entidades (Cliente, Conta, Transacao...)
│   │       └── dto/           # Objetos de transferência de dados
│   └── resources/
│       └── application.properties
```

---

## 📄 Exemplos de Endpoints

| Método | Endpoint            | Descrição                  |
|--------|---------------------|----------------------------|
| POST   | `/clientes`         | Cadastrar novo cliente     |
| GET    | `/clientes/{id}`    | Buscar cliente por ID      |
| POST   | `/contas`           | Criar conta bancária       |
| POST   | `/transacoes/deposito`   | Realizar depósito     |
| POST   | `/transacoes/saque`      | Realizar saque         |
| POST   | `/transacoes/transferencia` | Transferir entre contas |
| GET    | `/transacoes/{contaId}`  | Listar transações da conta |

---

## 👨‍💻 Autor

**Fabio Helmer**  
[GitHub](https://github.com/FabioHelmer)

---

> Projeto desenvolvido para fins de aprendizado e prática de backend com Spring Boot.
