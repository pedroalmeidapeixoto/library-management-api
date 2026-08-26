-- BANCO DE DADOS - SISTEMA DE BIBLIOTECA
-- PostgreSQL / Spring Boot
-- Execute conectado ao banco "postgres".

CREATE TABLE IF NOT EXISTS livros (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    ano_publicacao INTEGER,
    editora VARCHAR(255),
    genero VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    email VARCHAR(255),
    telefone VARCHAR(50),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'ATIVO'
);

CREATE TABLE IF NOT EXISTS exemplares (
    id BIGSERIAL PRIMARY KEY,
    id_livro BIGINT NOT NULL REFERENCES livros(id),
    localizacao VARCHAR(255),
    status VARCHAR(50) DEFAULT 'DISPONIVEL',
    data_aquisicao DATE
);

CREATE TABLE IF NOT EXISTS emprestimos (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    exemplar_id BIGINT NOT NULL REFERENCES exemplares(id),
    data_emprestimo DATE DEFAULT CURRENT_DATE,
    data_prevista_devolucao DATE,
    data_devolucao DATE,
    status VARCHAR(50) DEFAULT 'ATIVO'
);

CREATE TABLE IF NOT EXISTS multas (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    emprestimo_id BIGINT REFERENCES emprestimos(id),
    valor NUMERIC(10,2) NOT NULL DEFAULT 0,
    data_multa DATE DEFAULT CURRENT_DATE,
    status VARCHAR(50) DEFAULT 'PENDENTE'
);

CREATE INDEX IF NOT EXISTS idx_exemplares_livro ON exemplares(id_livro);
CREATE INDEX IF NOT EXISTS idx_emprestimos_usuario ON emprestimos(usuario_id);
CREATE INDEX IF NOT EXISTS idx_emprestimos_exemplar ON emprestimos(exemplar_id);
CREATE INDEX IF NOT EXISTS idx_multas_usuario ON multas(usuario_id);

-- Função usada para verificar disponibilidade.
CREATE OR REPLACE FUNCTION fn_exemplar_disponivel(p_exemplar_id BIGINT)
RETURNS BOOLEAN
LANGUAGE plpgsql
AS $$
DECLARE
    v_status VARCHAR(50);
BEGIN
    SELECT status INTO v_status
    FROM exemplares
    WHERE id = p_exemplar_id;

    IF NOT FOUND THEN
        RETURN FALSE;
    END IF;

    RETURN UPPER(v_status) = 'DISPONIVEL';
END;
$$;

-- Função para calcular multa: R$ 2,00 por dia de atraso.
CREATE OR REPLACE FUNCTION fn_calcular_multa(p_emprestimo_id BIGINT)
RETURNS NUMERIC(10,2)
LANGUAGE plpgsql
AS $$
DECLARE
    v_data_prevista DATE;
    v_data_devolucao DATE;
    v_dias_atraso INTEGER;
BEGIN
    SELECT data_prevista_devolucao,
           COALESCE(data_devolucao, CURRENT_DATE)
    INTO v_data_prevista, v_data_devolucao
    FROM emprestimos
    WHERE id = p_emprestimo_id;

    IF NOT FOUND OR v_data_prevista IS NULL THEN
        RETURN 0;
    END IF;

    v_dias_atraso := GREATEST(v_data_devolucao - v_data_prevista, 0);
    RETURN v_dias_atraso * 2.00;
END;
$$;

-- Trigger: empréstimo coloca exemplar como emprestado;
-- devolução coloca exemplar como disponível.
CREATE OR REPLACE FUNCTION fn_atualizar_status_exemplar()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE exemplares
        SET status = 'EMPRESTADO'
        WHERE id = NEW.exemplar_id;
    ELSIF TG_OP = 'UPDATE'
       AND NEW.status = 'DEVOLVIDO'
       AND OLD.status <> 'DEVOLVIDO' THEN
        UPDATE exemplares
        SET status = 'DISPONIVEL'
        WHERE id = NEW.exemplar_id;
    END IF;

    RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS trg_atualizar_status_exemplar ON emprestimos;

CREATE TRIGGER trg_atualizar_status_exemplar
AFTER INSERT OR UPDATE ON emprestimos
FOR EACH ROW
EXECUTE FUNCTION fn_atualizar_status_exemplar();

-- Trigger: gera multa quando uma devolução ocorre depois da data prevista.
CREATE OR REPLACE FUNCTION fn_criar_multa()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
DECLARE
    v_valor NUMERIC(10,2);
BEGIN
    IF NEW.data_devolucao IS NOT NULL
       AND NEW.data_prevista_devolucao IS NOT NULL
       AND NEW.data_devolucao > NEW.data_prevista_devolucao THEN

        v_valor := fn_calcular_multa(NEW.id);

        IF v_valor > 0 THEN
            INSERT INTO multas (usuario_id, emprestimo_id, valor, data_multa, status)
            VALUES (NEW.usuario_id, NEW.id, v_valor, CURRENT_DATE, 'PENDENTE');
        END IF;
    END IF;

    RETURN NEW;
END;
$$;

DROP TRIGGER IF EXISTS trg_criar_multa ON emprestimos;

CREATE TRIGGER trg_criar_multa
AFTER UPDATE ON emprestimos
FOR EACH ROW
WHEN (NEW.data_devolucao IS NOT NULL AND OLD.data_devolucao IS NULL)
EXECUTE FUNCTION fn_criar_multa();

-- Procedure usada pela rota:
-- POST /api/emprestimos/{id}/devolver
CREATE OR REPLACE PROCEDURE sp_devolver_livro(p_emprestimo_id BIGINT)
LANGUAGE plpgsql
AS $$
DECLARE
    v_exemplar_id BIGINT;
BEGIN
    SELECT exemplar_id INTO v_exemplar_id
    FROM emprestimos
    WHERE id = p_emprestimo_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Empréstimo % não encontrado', p_emprestimo_id;
    END IF;

    UPDATE emprestimos
    SET data_devolucao = CURRENT_DATE,
        status = 'DEVOLVIDO'
    WHERE id = p_emprestimo_id;

    UPDATE exemplares
    SET status = 'DISPONIVEL'
    WHERE id = v_exemplar_id;
END;
$$;

-- Dados mínimos para os testes do Postman.
INSERT INTO livros (id, titulo, ano_publicacao, editora, genero)
VALUES
    (1, 'Dom Casmurro', 1899, 'Livros do Brasil', 'Romance'),
    (2, 'O Cortiço', 1890, 'Ática', 'Romance'),
    (3, 'Memórias Póstumas de Brás Cubas', 1881, 'Penguin', 'Romance'),
    (4, 'Grande Sertão: Veredas', 1956, 'José Olympio', 'Romance')
ON CONFLICT (id) DO NOTHING;

INSERT INTO usuarios (id, nome, tipo, email, telefone, data_cadastro, status)
VALUES
    (1, 'Usuário Teste 1', 'ALUNO', 'usuario1@email.com', '(83) 99999-0001', CURRENT_TIMESTAMP, 'ATIVO'),
    (2, 'Usuário Teste 2', 'ALUNO', 'usuario2@email.com', '(83) 99999-0002', CURRENT_TIMESTAMP, 'ATIVO')
ON CONFLICT (id) DO NOTHING;

INSERT INTO exemplares (id, id_livro, localizacao, status, data_aquisicao)
VALUES
    (100, 1, 'Prateleira A1', 'DISPONIVEL', '2024-01-15'),
    (101, 2, 'Prateleira B2', 'EMPRESTADO', '2024-01-15'),
    (102, 3, 'Prateleira C3', 'DISPONIVEL', '2024-01-15')
ON CONFLICT (id) DO NOTHING;

-- Empréstimo 1 é usado pelo Postman para o teste de devolução.
INSERT INTO emprestimos
    (id, usuario_id, exemplar_id, data_emprestimo, data_prevista_devolucao, data_devolucao, status)
VALUES
    (1, 1, 101, CURRENT_DATE - INTERVAL '10 days',
     CURRENT_DATE - INTERVAL '5 days', NULL, 'ATIVO')
ON CONFLICT (id) DO NOTHING;

-- Ajusta as sequences após os IDs de teste.
SELECT setval(pg_get_serial_sequence('livros','id'), COALESCE((SELECT MAX(id) FROM livros),1), true);
SELECT setval(pg_get_serial_sequence('usuarios','id'), COALESCE((SELECT MAX(id) FROM usuarios),1), true);
SELECT setval(pg_get_serial_sequence('exemplares','id'), COALESCE((SELECT MAX(id) FROM exemplares),1), true);
SELECT setval(pg_get_serial_sequence('emprestimos','id'), COALESCE((SELECT MAX(id) FROM emprestimos),1), true);
SELECT setval(pg_get_serial_sequence('multas','id'), COALESCE((SELECT MAX(id) FROM multas),1), true);

-- Consultas de conferência:
-- SELECT * FROM livros;
-- SELECT * FROM usuarios;
-- SELECT * FROM exemplares;
-- SELECT * FROM emprestimos;
-- SELECT * FROM multas;
-- SELECT fn_exemplar_disponivel(102);
-- SELECT fn_calcular_multa(1);
