-- Align the baseline schema with the JPA model so Hibernate can validate it (ddl-auto=validate).
--   * PostgreSQL enums become VARCHAR + CHECK, matching the String / @Enumerated(STRING) fields
--   * identifiers used as Long become BIGINT, dates used as LocalDate become DATE
--   * columns the API already exposes but the baseline lacked are added

-- The view depends on the enum columns, so it is recreated at the end.
DROP VIEW public.vw_usuario_saldo;

-- usuario: values are stored as the Java enum names (ALUNO, ATIVO, ...)
ALTER TABLE public.usuario ALTER COLUMN tipo TYPE VARCHAR(20) USING UPPER(tipo::text);
ALTER TABLE public.usuario ALTER COLUMN status DROP DEFAULT;
ALTER TABLE public.usuario ALTER COLUMN status TYPE VARCHAR(10) USING UPPER(status::text);
ALTER TABLE public.usuario ALTER COLUMN status SET DEFAULT 'ATIVO';
UPDATE public.usuario SET status = 'ATIVO' WHERE status IS NULL;
ALTER TABLE public.usuario ALTER COLUMN status SET NOT NULL;
ALTER TABLE public.usuario
    ADD CONSTRAINT usuario_tipo_check CHECK (tipo IN ('ALUNO', 'PROFESSOR', 'FUNCIONARIO')),
    ADD CONSTRAINT usuario_status_check CHECK (status IN ('ATIVO', 'INATIVO'));

-- exemplar and emprestimo: statuses stay lowercase, as written by prc_realizar_devolucao
ALTER TABLE public.exemplar ALTER COLUMN status DROP DEFAULT;
ALTER TABLE public.exemplar ALTER COLUMN status TYPE VARCHAR(20) USING status::text;
ALTER TABLE public.exemplar ALTER COLUMN status SET DEFAULT 'disponivel';
ALTER TABLE public.exemplar
    ADD CONSTRAINT exemplar_status_check CHECK (status IN ('disponivel', 'emprestado', 'reservado'));

ALTER TABLE public.emprestimo ALTER COLUMN status DROP DEFAULT;
ALTER TABLE public.emprestimo ALTER COLUMN status TYPE VARCHAR(20) USING status::text;
ALTER TABLE public.emprestimo ALTER COLUMN status SET DEFAULT 'ativo';
ALTER TABLE public.emprestimo
    ADD CONSTRAINT emprestimo_status_check CHECK (status IN ('ativo', 'devolvido', 'atrasado'));

DROP TYPE public.tipo_usuario;
DROP TYPE public.status_usuario;
DROP TYPE public.status_exemplar;
DROP TYPE public.status_emprestimo;

-- identifiers mapped as Long
ALTER TABLE public.usuario ALTER COLUMN id_usuario TYPE BIGINT;
ALTER TABLE public.livro ALTER COLUMN id_livro TYPE BIGINT;
ALTER TABLE public.exemplar ALTER COLUMN id_exemplar TYPE BIGINT;
ALTER TABLE public.emprestimo ALTER COLUMN id_emprestimo TYPE BIGINT;
ALTER TABLE public.emprestimo ALTER COLUMN id_usuario TYPE BIGINT;
ALTER TABLE public.emprestimo ALTER COLUMN id_exemplar TYPE BIGINT;

-- types mapped as Integer / LocalDate
ALTER TABLE public.livro ALTER COLUMN ano_publicacao TYPE INTEGER;
ALTER TABLE public.emprestimo ALTER COLUMN data_emprestimo DROP DEFAULT;
ALTER TABLE public.emprestimo ALTER COLUMN data_emprestimo TYPE DATE USING data_emprestimo::date;
ALTER TABLE public.emprestimo ALTER COLUMN data_emprestimo SET DEFAULT CURRENT_DATE;

-- columns the entities and the API already use
ALTER TABLE public.livro ADD COLUMN data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE public.exemplar ADD COLUMN localizacao VARCHAR(50);
ALTER TABLE public.emprestimo ADD COLUMN observacao VARCHAR(255);

-- same definition as the baseline, comparing against plain text statuses
CREATE VIEW public.vw_usuario_saldo AS
 SELECT u.id_usuario,
    u.nome,
    u.email,
    u.tipo,
    u.status,
    COALESCE(active.count_act, (0)::bigint) AS total_emprestimos_ativos,
    COALESCE(hist.count_hist, (0)::bigint) AS total_emprestimos_historico,
    COALESCE(m.total_aberto, 0.00) AS total_multas_abertas,
    COALESCE(m.total_geral, 0.00) AS total_multas_geral,
    COALESCE(lastloan.ultima_data, NULL::date) AS ultima_data_emprestimo,
    COALESCE(titles.titulos, (ARRAY[]::text[])::character varying[]) AS titulos_emprestados
   FROM (((((public.usuario u
     LEFT JOIN ( SELECT emprestimo.id_usuario,
            count(*) AS count_act
           FROM public.emprestimo
          WHERE (emprestimo.status = 'ativo')
          GROUP BY emprestimo.id_usuario) active ON ((active.id_usuario = u.id_usuario)))
     LEFT JOIN ( SELECT emprestimo.id_usuario,
            count(*) AS count_hist
           FROM public.emprestimo
          GROUP BY emprestimo.id_usuario) hist ON ((hist.id_usuario = u.id_usuario)))
     LEFT JOIN ( SELECT e.id_usuario,
            sum(
                CASE
                    WHEN (m_1.pago = false) THEN m_1.valor
                    ELSE (0)::numeric
                END) AS total_aberto,
            sum(m_1.valor) AS total_geral
           FROM (public.multa m_1
             JOIN public.emprestimo e ON ((e.id_emprestimo = m_1.id_emprestimo)))
          GROUP BY e.id_usuario) m ON ((m.id_usuario = u.id_usuario)))
     LEFT JOIN ( SELECT emprestimo.id_usuario,
            max(emprestimo.data_emprestimo) AS ultima_data
           FROM public.emprestimo
          GROUP BY emprestimo.id_usuario) lastloan ON ((lastloan.id_usuario = u.id_usuario)))
     LEFT JOIN ( SELECT e.id_usuario,
            array_agg(DISTINCT l.titulo ORDER BY l.titulo) FILTER (WHERE (emp.status = 'ativo')) AS titulos
           FROM (((public.emprestimo emp
             JOIN public.exemplar ex ON ((ex.id_exemplar = emp.id_exemplar)))
             JOIN public.livro l ON ((l.id_livro = ex.id_livro)))
             JOIN public.emprestimo e ON ((e.id_emprestimo = emp.id_emprestimo)))
          GROUP BY e.id_usuario, emp.status) titles ON ((titles.id_usuario = u.id_usuario)));
