-- Optional sample data for local development and the Postman collection.
-- Run after the application has started once (Flyway creates the schema):
--   psql -d biblioteca -f database/dev_seed.sql

INSERT INTO livro (id_livro, titulo, ano_publicacao, editora, genero) VALUES
    (1, 'Dom Casmurro', 1899, 'Livros do Brasil', 'Romance'),
    (2, 'O Cortiço', 1890, 'Ática', 'Romance'),
    (3, 'Memórias Póstumas de Brás Cubas', 1881, 'Penguin', 'Romance'),
    (4, 'Grande Sertão: Veredas', 1956, 'José Olympio', 'Romance');

INSERT INTO usuario (id_usuario, nome, tipo, email, telefone) VALUES
    (1, 'Usuário Teste 1', 'ALUNO', 'usuario1@email.com', '(83) 99999-0001'),
    (2, 'Usuário Teste 2', 'ALUNO', 'usuario2@email.com', '(83) 99999-0002');

INSERT INTO exemplar (id_exemplar, id_livro, localizacao, status, data_aquisicao) VALUES
    (100, 1, 'Prateleira A1', 'disponivel', '2024-01-15'),
    (101, 2, 'Prateleira B2', 'disponivel', '2024-01-15'),
    (102, 3, 'Prateleira C3', 'disponivel', '2024-01-15');

-- Loan 1 is overdue and is used by the Postman return request.
-- Inserting it fires the loan triggers, which mark copy 101 as 'emprestado'.
INSERT INTO emprestimo (id_emprestimo, id_usuario, id_exemplar, data_emprestimo, data_devolucao_prevista, status)
VALUES (1, 1, 101, CURRENT_DATE - 10, CURRENT_DATE - 5, 'ativo');

SELECT setval(pg_get_serial_sequence('livro', 'id_livro'), (SELECT MAX(id_livro) FROM livro));
SELECT setval(pg_get_serial_sequence('usuario', 'id_usuario'), (SELECT MAX(id_usuario) FROM usuario));
SELECT setval(pg_get_serial_sequence('exemplar', 'id_exemplar'), (SELECT MAX(id_exemplar) FROM exemplar));
SELECT setval(pg_get_serial_sequence('emprestimo', 'id_emprestimo'), (SELECT MAX(id_emprestimo) FROM emprestimo));
