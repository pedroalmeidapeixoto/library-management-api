package com.biblioteca.mapper;

import com.biblioteca.dto.request.LivroRequestDTO;
import com.biblioteca.dto.response.LivroResponseDTO;
import com.biblioteca.model.Livro;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class LivroMapperTest {

    private final LivroMapper mapper = Mappers.getMapper(LivroMapper.class);

    @Test
    void testToEntity() {
        LivroRequestDTO dto = new LivroRequestDTO();
        dto.setTitulo("O Hobbit");
        dto.setAutor("Tolkien");
        dto.setAnoPublicacao(1937);

        Livro livro = mapper.toEntity(dto);

        assertNotNull(livro);
        assertEquals("O Hobbit", livro.getTitulo());
        assertEquals("Tolkien", livro.getAutor());
        assertEquals(1937, livro.getAnoPublicacao());
    }

    @Test
    void testToResponseDTO() {
        Livro livro = new Livro();
        livro.setId(10L);
        livro.setTitulo("1984");
        livro.setAutor("George Orwell");
        livro.setAnoPublicacao(1949);

        LivroResponseDTO dto = mapper.toResponseDTO(livro);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals("1984", dto.getTitulo());
        assertEquals("George Orwell", dto.getAutor());
        assertEquals(1949, dto.getAnoPublicacao());
    }
}
