package com.biblioteca.mapper;

import com.biblioteca.dto.request.UsuarioRequestDTO;
import com.biblioteca.dto.response.UsuarioResponseDTO;
import com.biblioteca.mapper.UsuarioMapper;
import com.biblioteca.model.Usuario;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioMapperTest {

    private final UsuarioMapper mapper = Mappers.getMapper(UsuarioMapper.class);

    @Test
    void testToEntity() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNome("Pedro");
        dto.setEmail("pedro@example.com");
        dto.setMatricula("2024001");

        Usuario usuario = mapper.toEntity(dto);

        assertNotNull(usuario);
        assertEquals("Pedro", usuario.getNome());
        assertEquals("pedro@example.com", usuario.getEmail());
        assertEquals("2024001", usuario.getMatricula());
    }

    @Test
    void testToResponseDTO() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Maria");
        usuario.setEmail("maria@example.com");
        usuario.setMatricula("2023007");

        UsuarioResponseDTO dto = mapper.toResponseDTO(usuario);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Maria", dto.getNome());
        assertEquals("maria@example.com", dto.getEmail());
        assertEquals("2023007", dto.getMatricula());
    }
}
