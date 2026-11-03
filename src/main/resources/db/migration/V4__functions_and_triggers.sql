-- Database-side objects the application depends on:
--   * fn_exemplar_disponivel, fn_calcular_total_multa_pendente, fn_multas_usuario (called by DevolucaoService)
--   * trg_validar_emprestimo, trg_marcar_exemplar_emprestado, trg_auditoria_emprestimo (fired when a loan is saved)

CREATE FUNCTION public.fn_exemplar_disponivel(p_id_exemplar integer)
RETURNS boolean
LANGUAGE sql
STABLE
AS $$
    SELECT COALESCE((SELECT status = 'disponivel' FROM exemplar WHERE id_exemplar = p_id_exemplar), FALSE);
$$;

CREATE FUNCTION public.fn_calcular_total_multa_pendente(p_id_usuario integer)
RETURNS numeric
LANGUAGE sql
STABLE
AS $$
    SELECT COALESCE(SUM(m.valor), 0)
    FROM multa m
    JOIN emprestimo e ON e.id_emprestimo = m.id_emprestimo
    WHERE e.id_usuario = p_id_usuario
      AND m.pago = FALSE;
$$;

CREATE FUNCTION public.fn_multas_usuario(p_id_usuario integer)
RETURNS TABLE (
    quantidade_multa_pendente bigint,
    quantidade_multa_paga bigint,
    total_pendente numeric,
    total_pago numeric
)
LANGUAGE sql
STABLE
AS $$
    SELECT COUNT(*) FILTER (WHERE m.pago = FALSE),
           COUNT(*) FILTER (WHERE m.pago = TRUE),
           COALESCE(SUM(m.valor) FILTER (WHERE m.pago = FALSE), 0),
           COALESCE(SUM(m.valor) FILTER (WHERE m.pago = TRUE), 0)
    FROM multa m
    JOIN emprestimo e ON e.id_emprestimo = m.id_emprestimo
    WHERE e.id_usuario = p_id_usuario;
$$;

-- A loan can only be created for a copy that is currently available.
-- The row lock serializes concurrent loans of the same copy.
CREATE FUNCTION public.fn_validar_emprestimo()
RETURNS trigger
LANGUAGE plpgsql
AS $$
DECLARE
    v_status VARCHAR(20);
BEGIN
    SELECT status INTO v_status
    FROM exemplar
    WHERE id_exemplar = NEW.id_exemplar
    FOR UPDATE;

    IF v_status IS DISTINCT FROM 'disponivel' THEN
        RAISE EXCEPTION 'Exemplar % não está disponível para empréstimo.', NEW.id_exemplar;
    END IF;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_validar_emprestimo
BEFORE INSERT ON public.emprestimo
FOR EACH ROW
EXECUTE FUNCTION public.fn_validar_emprestimo();

CREATE FUNCTION public.fn_marcar_exemplar_emprestado()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE exemplar SET status = 'emprestado' WHERE id_exemplar = NEW.id_exemplar;
    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_marcar_exemplar_emprestado
AFTER INSERT ON public.emprestimo
FOR EACH ROW
EXECUTE FUNCTION public.fn_marcar_exemplar_emprestado();

CREATE FUNCTION public.fn_auditoria_emprestimo()
RETURNS trigger
LANGUAGE plpgsql
AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO auditoria_emprestimo (id_emprestimo, evento, usuario_operacao, detalhe)
        VALUES (NEW.id_emprestimo, 'INSERT', current_user, 'status: ' || NEW.status);
    ELSE
        INSERT INTO auditoria_emprestimo (id_emprestimo, evento, usuario_operacao, detalhe)
        VALUES (NEW.id_emprestimo, 'UPDATE', current_user, 'status: ' || OLD.status || ' -> ' || NEW.status);
    END IF;

    RETURN NEW;
END;
$$;

CREATE TRIGGER trg_auditoria_emprestimo
AFTER INSERT OR UPDATE ON public.emprestimo
FOR EACH ROW
EXECUTE FUNCTION public.fn_auditoria_emprestimo();
