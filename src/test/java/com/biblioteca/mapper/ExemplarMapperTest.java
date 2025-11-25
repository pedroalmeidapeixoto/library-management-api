package com.biblioteca.mapper;

import com.biblioteca.dto.request.ExemplarRequestDTO;
import com.biblioteca.dto.response.ExemplarResponseDTO;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Livro;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class ExemplarMapperTest {

    private final ExemplarMapper mapper = Mappers.getMapper(ExemplarMapper.class);

    @Test
    void testToEntity() {
        ExemplarRequestDTO dto = new ExemplarRequestDTO();
        dto.setIdLivro(5L);

        Exemplar exemplar = mapper.toEntity(dto);

        assertNotNull(exemplar);
        assertNotNull(exemplar.getLivro());
        assertEquals(5L, exemplar.getLivro().getId());
    }

    @Test
    void testToResponseDTO() {
        Livro livro = new Livro();
        livro.setId(2L);

        Exemplar exemplar = new Exemplar();
        exemplar.setId(99L);
        exemplar.setLivro(livro);

        ExemplarResponseDTO dto = mapper.toResponseDTO(exemplar);

        assertNotNull(dto);
        assertEquals(99L, dto.getId());
        assertEquals(2L, dto.getIdLivro());
    }
}
