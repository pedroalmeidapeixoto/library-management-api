package com.biblioteca.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DevolucaoServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private DevolucaoService service;

    @Test
    void shouldCallReturnProcedureWithLoanId() {
        service.realizarDevolucao(7);

        verify(jdbcTemplate).update("CALL prc_realizar_devolucao(?)", 7);
    }

    @Test
    void shouldReturnPendingFineTotal() {
        when(jdbcTemplate.queryForObject(
                eq("SELECT fn_calcular_total_multa_pendente(?)"), eq(Double.class), eq(3)))
                .thenReturn(12.5);

        assertEquals(12.5, service.calcularTotalMultaPendente(3));
    }

    @Test
    void shouldReturnZeroWhenFineTotalIsNull() {
        when(jdbcTemplate.queryForObject(
                eq("SELECT fn_calcular_total_multa_pendente(?)"), eq(Double.class), eq(3)))
                .thenReturn(null);

        assertEquals(0.0, service.calcularTotalMultaPendente(3));
    }

    @Test
    void shouldReportCopyAsAvailable() {
        when(jdbcTemplate.queryForObject(
                eq("SELECT fn_exemplar_disponivel(?)"), eq(Boolean.class), eq(102)))
                .thenReturn(true);

        assertTrue(service.verificarDisponibilidade(102));
    }

    @Test
    void shouldReportCopyAsUnavailableWhenFunctionReturnsNull() {
        when(jdbcTemplate.queryForObject(
                eq("SELECT fn_exemplar_disponivel(?)"), eq(Boolean.class), eq(102)))
                .thenReturn(null);

        assertFalse(service.verificarDisponibilidade(102));
    }

    @Test
    void shouldConvertFineCountsToInteger() {
        Map<String, Object> row = new HashMap<>();
        row.put("quantidade_multa_pendente", 2L);
        row.put("quantidade_multa_paga", 5L);
        when(jdbcTemplate.queryForMap("SELECT * FROM fn_multas_usuario(?)", 1)).thenReturn(row);

        Map<String, Object> result = service.getMultasUsuario(1);

        assertEquals(2, result.get("quantidade_multa_pendente"));
        assertEquals(5, result.get("quantidade_multa_paga"));
    }
}
