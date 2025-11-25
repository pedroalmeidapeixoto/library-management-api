package com.biblioteca.mapper;

import com.biblioteca.dto.response.MultaResponseDTO;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Multa;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class MultaMapperTest {

    private final MultaMapper mapper = Mappers.getMapper(MultaMapper.class);

    @Test
    void testToResponseDTO() {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(50L);

        Multa multa = new Multa();
        multa.setId(200L);
        multa.setValor(35.0);
        multa.setEmprestimo(emprestimo);

        MultaResponseDTO dto = mapper.toResponseDTO(multa);

        assertNotNull(dto);
        assertEquals(200L, dto.getId());
        assertEquals(35.0, dto.getValor());
        assertEquals(50L, dto.getIdEmprestimo());
    }
}
