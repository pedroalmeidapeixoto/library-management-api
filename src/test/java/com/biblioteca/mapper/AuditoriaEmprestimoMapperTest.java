package com.biblioteca.mapper;

import com.biblioteca.dto.response.AuditoriaEmprestimoResponseDTO;
import com.biblioteca.model.AuditoriaEmprestimo;
import com.biblioteca.model.Emprestimo;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AuditoriaEmprestimoMapperTest {

    private final AuditoriaEmprestimoMapper mapper = Mappers.getMapper(AuditoriaEmprestimoMapper.class);

    @Test
    void testToResponseDTO() {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(500L);

        AuditoriaEmprestimo auditoria = new AuditoriaEmprestimo();
        auditoria.setId(300L);
        auditoria.setEmprestimo(emprestimo);
        auditoria.setDataEvento(LocalDate.of(2025, 1, 15));
        auditoria.setTipoEvento("DEVOLUCAO");

        AuditoriaEmprestimoResponseDTO dto = mapper.toResponseDTO(auditoria);

        assertNotNull(dto);
        assertEquals(300L, dto.getId());
        assertEquals(500L, dto.getIdEmprestimo());
        assertEquals(LocalDate.of(2025, 1, 15), dto.getDataEvento());
        assertEquals("DEVOLUCAO", dto.getTipoEvento());
    }
}
