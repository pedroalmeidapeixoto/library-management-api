📚 Biblioteca Universitária – Sistema de Empréstimos

API desenvolvida em Java + Spring Boot, com arquitetura limpa, DTOs, mappers MapStruct, validações, camadas separadas e integração com banco PostgreSQL (Supabase).

🚀 Tecnologias Utilizadas

Java 17

Spring Boot 3

Spring Web

Spring Data JPA

PostgreSQL (Supabase)

Lombok

MapStruct

Flyway (opcional)

Jackson (configurado)

Arquitetura RESTful

🏛 Arquitetura da Aplicação

A aplicação segue uma arquitetura limpa por camadas:

src/main/java/com/biblioteca
│
├── config/           → Configurações globais (CORS, Jackson etc.)
│
├── controller/       → Entrada da API (REST Controllers)
│
├── dto/              → DTOs (Request/Response)
│
├── exception/        → Exceptions customizadas + Exception Handler
│
├── mapper/           → MapStruct Mappers
│
├── model/            → Entidades JPA
│
├── repository/       → Interfaces JPA Repository
│
├── service/          → Lógica de negócio
│
└── util/             → Utilidades (data, validações etc.)

📄 Funcionalidades Principais
👤 Usuários

Criar usuário

Listar usuários

Buscar por ID

Excluir usuário

📚 Livros

Cadastro de livros

Listagem

Busca por ID

📘 Exemplares

Criar exemplar vinculado a um livro

Listar exemplares

Buscar por ID

📆 Reservas

Criar reserva de exemplar

Listar reservas

🔄 Empréstimos

Realizar empréstimo

Realizar devolução

Listar empréstimos

💰 Multas

Listar multas

Buscar multa por ID

📝 Auditorias

Listar auditorias

Buscar auditoria por ID
(modo somente leitura; registra empréstimos e devoluções)

🧱 Estrutura das Entidades (resumo)

O sistema implementa entidades:

Usuario

Livro

Exemplar

Reserva

Emprestimo

Multa

AuditoriaEmprestimo

Com relacionamentos:

Livro 1—N Exemplar

Usuario 1—N Emprestimo

Exemplar 1—N Emprestimo

Emprestimo 1—1 Multa

Reserva (Usuario + Exemplar)

🧩 MapStruct

Toda a conversão entre Entidade ↔ DTO é feita automaticamente pelos mappers:

UsuarioMapper
LivroMapper
ExemplarMapper
ReservaMapper
EmprestimoMapper
MultaMapper
AuditoriaMapper

🛠 Configurações Importantes
🔧 Configurações adicionadas

CORS liberado (CORSConfig)

Configuração global do Jackson (datas, serialização)

AuditConfig (estrutura para auditoria futura)

🧰 Utilidades incluídas

DateUtils (cálculo de dias)

ValidationUtils (validação genérica)

UUIDUtils (geração de códigos)

🗄 Banco de Dados (Supabase)

Para rodar o projeto, configure o seu application.properties:

spring.datasource.url=jdbc:postgresql://<supabase-url>:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=<sua-senha>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

▶ Como Rodar

No terminal (ou pelo IntelliJ):

mvn spring-boot:run


Ou:

./mvnw spring-boot:run

📬 Endpoints Principais
Usuários

POST /usuarios
GET /usuarios
GET /usuarios/{id}
DELETE /usuarios/{id}

Livros

POST /livros
GET /livros
GET /livros/{id}

Exemplares

POST /exemplares
GET /exemplares
GET /exemplares/{id}

Reservas

POST /reservas?idUsuario=&idExemplar=
GET /reservas

Empréstimos

POST /emprestimos?idUsuario=&idExemplar=
POST /emprestimos/devolucao/{id}
GET /emprestimos

Multas

GET /multas
GET /multas/{id}

Auditorias

GET /auditorias
GET /auditorias/{id}

👨‍💻 Desenvolvedores

Projeto desenvolvido para disciplina de Banco de Dados com Programação Orientada a Objetos.

