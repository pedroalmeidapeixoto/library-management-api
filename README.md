📚 Biblioteca – Sistema de Gerenciamento (Spring Boot + SQL)

Este projeto implementa um sistema completo de gerenciamento de uma biblioteca, utilizando Java com Spring Boot, banco de dados PostgreSQL e comunicação via API REST.
A aplicação inclui controle de usuários, livros, exemplares, empréstimos, devoluções e cálculo de multas, integrando lógica de negócio com procedures SQL.

🚀 Tecnologias Utilizadas

Java 17+

Spring Boot

Spring Web

Spring Data JPA

Exception Handler

DTO + Mapper

PostgreSQL

Maven

Procedures em PL/pgSQL

Lombok (para reduzir boilerplate)

📁 Estrutura do Projeto
BD-POO-Pierre/
├── src/
│   ├── main/java/com/biblioteca/
│   │   ├── config/          # Configurações gerais (ex: CORS)
│   │   ├── controller/      # Endpoints REST
│   │   ├── dto/             # DTOs de entrada e saída
│   │   ├── exception/       # Tratamento global de erros
│   │   ├── mapper/          # Conversores DTO <-> Entidade
│   │   ├── model/           # Entidades JPA
│   │   ├── repository/      # Repositórios JPA
│   │   ├── service/         # Regras de negócio
│   │   └── BibliotecaApplication.java
│   └── resources/
│       └── application.properties
└── pom.xml

🛢️ Banco de Dados

O projeto utiliza PostgreSQL com as seguintes principais entidades:

Usuario

Livro

Exemplar

Emprestimo

Devolucao

📌 Procedure utilizada no sistema

A aplicação faz uso direto da seguinte procedure para tratar devoluções:

CREATE OR REPLACE PROCEDURE prc_realizar_devolucao(p_id_emprestimo INT)
LANGUAGE plpgsql
AS $$
DECLARE
v_id_exemplar INT;
v_data_prevista DATE;
[...]
$$;


(O conteúdo completo está no arquivo SQL enviado pelo usuário.)

🧩 Funcionalidades
✔️ Usuários

Cadastro, edição, listagem e remoção

Tipos de usuário (aluno, professor etc.)

Status (ativo/inativo)

✔️ Livros e Exemplares

Cadastro de livros

Controle de exemplares

Associação livro → exemplar

✔️ Empréstimos

Criação de empréstimo

Cálculo de datas previstas

✔️ Devoluções

Integração com procedure SQL prc_realizar_devolucao

Cálculo e retorno de multa

✔️ Respostas padronizadas

DTOs organizados

DTOs de erro (ErrorResponse)

DTOs de sucesso (LivroResponseDTO, UsuarioResponseDTO, TotalMultaResponse, etc.)

🔐 CORS / Configuração

O projeto possui CORSConfig configurado para permitir acesso pelo frontend:

@Bean
public WebMvcConfigurer corsConfigurer() { ... }

▶️ Como Rodar o Projeto
1. Clone o repositório
   git clone <seu-repositorio>
   cd BD-POO-Pierre

2. Configure o banco de dados

No arquivo application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_supermercado
spring.datasource.username=postgres
spring.datasource.password=SUASENHA
spring.jpa.hibernate.ddl-auto=update


O usuário pediu anteriormente para usar o nome do banco ecommerce_supermercado, então o exemplo já está ajustado.

3. Execute o projeto
   mvn spring-boot:run

🌐 Endpoints Principais
🔹 Usuários
POST   /usuarios
GET    /usuarios
PUT    /usuarios/{id}
DELETE /usuarios/{id}

🔹 Livros
POST   /livros
GET    /livros
PUT    /livros/{id}
DELETE /livros/{id}

🔹 Empréstimos / Devoluções
POST /emprestimos
POST /devolucoes/{idEmprestimo}

🧪 Testes

Os endpoints podem ser testados via:

Insomnia

Postman

Thunder Client

Swagger (caso habilitado futuramente)

Projeto desenvolvido para estudos de Banco de Dados, POO e Integração Back-end + SQL.