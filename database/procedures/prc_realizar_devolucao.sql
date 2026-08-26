CREATE OR REPLACE PROCEDURE public.prc_realizar_devolucao(IN p_id_emprestimo integer)
LANGUAGE plpgsql
AS $procedure$
DECLARE
    v_id_exemplar INT;
    v_data_prevista DATE;
    v_data_real DATE := CURRENT_DATE;
    v_dias_atraso INT;
    v_valor_multa NUMERIC(10,2);
BEGIN
    SELECT id_exemplar, data_devolucao_prevista
    INTO v_id_exemplar, v_data_prevista
    FROM emprestimo
    WHERE id_emprestimo = p_id_emprestimo;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Empréstimo % não existe.', p_id_emprestimo;
    END IF;

    UPDATE emprestimo
    SET data_devolucao_real = v_data_real
    WHERE id_emprestimo = p_id_emprestimo;

    v_dias_atraso := (v_data_real - v_data_prevista);

    IF v_dias_atraso > 0 THEN
        v_valor_multa := v_dias_atraso * 1.50;

        INSERT INTO multa (id_emprestimo, valor, data_aplicacao, pago)
        VALUES (p_id_emprestimo, v_valor_multa, v_data_real, FALSE);

        UPDATE emprestimo
        SET status = 'atrasado'
        WHERE id_emprestimo = p_id_emprestimo;
    ELSE
        UPDATE emprestimo
        SET status = 'devolvido'
        WHERE id_emprestimo = p_id_emprestimo;
    END IF;

    UPDATE exemplar
    SET status = 'disponivel'
    WHERE id_exemplar = v_id_exemplar;
END;
$procedure$;
