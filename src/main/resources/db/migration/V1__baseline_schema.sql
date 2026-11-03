-- Baseline: schema of the library database as it existed before Flyway was introduced.
-- Taken from the local PostgreSQL 16 database (schema only, no data).

CREATE TYPE public.status_emprestimo AS ENUM (
    'ativo',
    'devolvido',
    'atrasado'
);

CREATE TYPE public.status_exemplar AS ENUM (
    'disponivel',
    'emprestado',
    'reservado'
);

CREATE TYPE public.status_reserva AS ENUM (
    'ativa',
    'expirada',
    'atendida',
    'cancelada'
);

CREATE TYPE public.status_usuario AS ENUM (
    'ativo',
    'inativo'
);

CREATE TYPE public.tipo_usuario AS ENUM (
    'aluno',
    'professor',
    'funcionario'
);

CREATE TABLE public.auditoria_emprestimo (
    id_auditoria integer NOT NULL,
    id_emprestimo integer,
    evento character varying(60) NOT NULL,
    usuario_operacao character varying(120),
    data_evento timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    detalhe text
);

CREATE SEQUENCE public.auditoria_emprestimo_id_auditoria_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.auditoria_emprestimo_id_auditoria_seq OWNED BY public.auditoria_emprestimo.id_auditoria;

CREATE TABLE public.autor (
    id_autor integer NOT NULL,
    nome character varying(120) NOT NULL
);

CREATE SEQUENCE public.autor_id_autor_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.autor_id_autor_seq OWNED BY public.autor.id_autor;

CREATE TABLE public.emprestimo (
    id_emprestimo integer NOT NULL,
    id_exemplar integer NOT NULL,
    id_usuario integer NOT NULL,
    data_emprestimo timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    data_devolucao_prevista date,
    data_devolucao_real date,
    status public.status_emprestimo DEFAULT 'ativo'::public.status_emprestimo,
    CONSTRAINT emprestimo_check CHECK (((data_devolucao_real IS NULL) OR (data_devolucao_real >= (data_emprestimo)::date)))
);

CREATE SEQUENCE public.emprestimo_id_emprestimo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.emprestimo_id_emprestimo_seq OWNED BY public.emprestimo.id_emprestimo;

CREATE TABLE public.exemplar (
    id_exemplar integer NOT NULL,
    id_livro integer NOT NULL,
    codigo_exemplar character varying(80),
    status public.status_exemplar DEFAULT 'disponivel'::public.status_exemplar,
    data_aquisicao date
);

CREATE SEQUENCE public.exemplar_id_exemplar_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.exemplar_id_exemplar_seq OWNED BY public.exemplar.id_exemplar;

CREATE TABLE public.livro (
    id_livro integer NOT NULL,
    titulo character varying(250) NOT NULL,
    ano_publicacao smallint,
    editora character varying(120),
    genero character varying(80),
    CONSTRAINT livro_ano_publicacao_check CHECK (((ano_publicacao >= 1000) AND (ano_publicacao <= (EXTRACT(year FROM CURRENT_DATE))::integer)))
);

CREATE TABLE public.livro_autor (
    id_livro_autor integer NOT NULL,
    id_livro integer NOT NULL,
    id_autor integer NOT NULL,
    ordem_autor smallint DEFAULT 1,
    CONSTRAINT livro_autor_ordem_autor_check CHECK ((ordem_autor > 0))
);

CREATE SEQUENCE public.livro_autor_id_livro_autor_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.livro_autor_id_livro_autor_seq OWNED BY public.livro_autor.id_livro_autor;

CREATE SEQUENCE public.livro_id_livro_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.livro_id_livro_seq OWNED BY public.livro.id_livro;

CREATE TABLE public.multa (
    id_multa integer NOT NULL,
    id_emprestimo integer NOT NULL,
    valor numeric(10,2) NOT NULL,
    data_aplicacao timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    pago boolean DEFAULT false,
    observacao text,
    CONSTRAINT multa_valor_check CHECK ((valor >= (0)::numeric))
);

CREATE SEQUENCE public.multa_id_multa_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.multa_id_multa_seq OWNED BY public.multa.id_multa;

CREATE TABLE public.reserva (
    id_reserva integer NOT NULL,
    id_exemplar integer NOT NULL,
    id_usuario integer NOT NULL,
    data_reserva timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    data_validade timestamp without time zone,
    status public.status_reserva DEFAULT 'ativa'::public.status_reserva
);

CREATE SEQUENCE public.reserva_id_reserva_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.reserva_id_reserva_seq OWNED BY public.reserva.id_reserva;

CREATE TABLE public.usuario (
    id_usuario integer NOT NULL,
    nome character varying(120) NOT NULL,
    tipo public.tipo_usuario NOT NULL,
    email character varying(200),
    telefone character varying(30),
    data_cadastro timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    status public.status_usuario DEFAULT 'ativo'::public.status_usuario
);

CREATE SEQUENCE public.usuario_id_usuario_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.usuario_id_usuario_seq OWNED BY public.usuario.id_usuario;

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
    COALESCE(lastloan.ultima_data, NULL::timestamp without time zone) AS ultima_data_emprestimo,
    COALESCE(titles.titulos, (ARRAY[]::text[])::character varying[]) AS titulos_emprestados
   FROM (((((public.usuario u
     LEFT JOIN ( SELECT emprestimo.id_usuario,
            count(*) AS count_act
           FROM public.emprestimo
          WHERE (emprestimo.status = 'ativo'::public.status_emprestimo)
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
            array_agg(DISTINCT l.titulo ORDER BY l.titulo) FILTER (WHERE (emp.status = 'ativo'::public.status_emprestimo)) AS titulos
           FROM (((public.emprestimo emp
             JOIN public.exemplar ex ON ((ex.id_exemplar = emp.id_exemplar)))
             JOIN public.livro l ON ((l.id_livro = ex.id_livro)))
             JOIN public.emprestimo e ON ((e.id_emprestimo = emp.id_emprestimo)))
          GROUP BY e.id_usuario, emp.status) titles ON ((titles.id_usuario = u.id_usuario)));

ALTER TABLE ONLY public.auditoria_emprestimo ALTER COLUMN id_auditoria SET DEFAULT nextval('public.auditoria_emprestimo_id_auditoria_seq'::regclass);

ALTER TABLE ONLY public.autor ALTER COLUMN id_autor SET DEFAULT nextval('public.autor_id_autor_seq'::regclass);

ALTER TABLE ONLY public.emprestimo ALTER COLUMN id_emprestimo SET DEFAULT nextval('public.emprestimo_id_emprestimo_seq'::regclass);

ALTER TABLE ONLY public.exemplar ALTER COLUMN id_exemplar SET DEFAULT nextval('public.exemplar_id_exemplar_seq'::regclass);

ALTER TABLE ONLY public.livro ALTER COLUMN id_livro SET DEFAULT nextval('public.livro_id_livro_seq'::regclass);

ALTER TABLE ONLY public.livro_autor ALTER COLUMN id_livro_autor SET DEFAULT nextval('public.livro_autor_id_livro_autor_seq'::regclass);

ALTER TABLE ONLY public.multa ALTER COLUMN id_multa SET DEFAULT nextval('public.multa_id_multa_seq'::regclass);

ALTER TABLE ONLY public.reserva ALTER COLUMN id_reserva SET DEFAULT nextval('public.reserva_id_reserva_seq'::regclass);

ALTER TABLE ONLY public.usuario ALTER COLUMN id_usuario SET DEFAULT nextval('public.usuario_id_usuario_seq'::regclass);

ALTER TABLE ONLY public.auditoria_emprestimo
    ADD CONSTRAINT auditoria_emprestimo_pkey PRIMARY KEY (id_auditoria);

ALTER TABLE ONLY public.autor
    ADD CONSTRAINT autor_pkey PRIMARY KEY (id_autor);

ALTER TABLE ONLY public.emprestimo
    ADD CONSTRAINT emprestimo_pkey PRIMARY KEY (id_emprestimo);

ALTER TABLE ONLY public.exemplar
    ADD CONSTRAINT exemplar_codigo_exemplar_key UNIQUE (codigo_exemplar);

ALTER TABLE ONLY public.exemplar
    ADD CONSTRAINT exemplar_pkey PRIMARY KEY (id_exemplar);

ALTER TABLE ONLY public.livro_autor
    ADD CONSTRAINT livro_autor_id_livro_id_autor_key UNIQUE (id_livro, id_autor);

ALTER TABLE ONLY public.livro_autor
    ADD CONSTRAINT livro_autor_pkey PRIMARY KEY (id_livro_autor);

ALTER TABLE ONLY public.livro
    ADD CONSTRAINT livro_pkey PRIMARY KEY (id_livro);

ALTER TABLE ONLY public.multa
    ADD CONSTRAINT multa_pkey PRIMARY KEY (id_multa);

ALTER TABLE ONLY public.reserva
    ADD CONSTRAINT reserva_pkey PRIMARY KEY (id_reserva);

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_email_key UNIQUE (email);

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id_usuario);

CREATE INDEX idx_emprestimo_exemplar_status ON public.emprestimo USING btree (id_exemplar, status, data_devolucao_prevista);

CREATE INDEX idx_emprestimo_usuario ON public.emprestimo USING btree (id_usuario);

CREATE INDEX idx_exemplar_livro ON public.exemplar USING btree (id_livro);

CREATE INDEX idx_multa_pago ON public.multa USING btree (pago);

CREATE INDEX idx_reserva_exemplar_status ON public.reserva USING btree (id_exemplar, status);

ALTER TABLE ONLY public.auditoria_emprestimo
    ADD CONSTRAINT auditoria_emprestimo_id_emprestimo_fkey FOREIGN KEY (id_emprestimo) REFERENCES public.emprestimo(id_emprestimo) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY public.emprestimo
    ADD CONSTRAINT emprestimo_id_exemplar_fkey FOREIGN KEY (id_exemplar) REFERENCES public.exemplar(id_exemplar) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY public.emprestimo
    ADD CONSTRAINT emprestimo_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY public.exemplar
    ADD CONSTRAINT exemplar_id_livro_fkey FOREIGN KEY (id_livro) REFERENCES public.livro(id_livro) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY public.livro_autor
    ADD CONSTRAINT livro_autor_id_autor_fkey FOREIGN KEY (id_autor) REFERENCES public.autor(id_autor) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY public.livro_autor
    ADD CONSTRAINT livro_autor_id_livro_fkey FOREIGN KEY (id_livro) REFERENCES public.livro(id_livro) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY public.multa
    ADD CONSTRAINT multa_id_emprestimo_fkey FOREIGN KEY (id_emprestimo) REFERENCES public.emprestimo(id_emprestimo) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY public.reserva
    ADD CONSTRAINT reserva_id_exemplar_fkey FOREIGN KEY (id_exemplar) REFERENCES public.exemplar(id_exemplar) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY public.reserva
    ADD CONSTRAINT reserva_id_usuario_fkey FOREIGN KEY (id_usuario) REFERENCES public.usuario(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT;
