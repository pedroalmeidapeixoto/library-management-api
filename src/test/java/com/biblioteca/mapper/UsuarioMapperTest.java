package com.biblioteca.mapper;

import com.biblioteca.dto.usuario.UsuarioDTO;
import com.biblioteca.dto.usuario.UsuarioResponseDTO;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.enums.StatusUsuario;
import com.biblioteca.model.enums.TipoUsuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioMapperTest {

    private final UsuarioMapper mapper = new UsuarioMapper();

    @Test
    void shouldMapDtoToEntity() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Pedro");
        dto.setTipo(TipoUsuario.ALUNO);
        dto.setEmail("pedro@example.com");
        dto.setTelefone("83999999999");
        dto.setStatus(StatusUsuario.ATIVO);

        Usuario usuario = mapper.toEntity(dto);

        assertNotNull(usuario);
        assertEquals("Pedro", usuario.getNome());
        assertEquals(TipoUsuario.ALUNO, usuario.getTipo());
        assertEquals("pedro@example.com", usuario.getEmail());
        assertEquals("83999999999", usuario.getTelefone());
        assertEquals(StatusUsuario.ATIVO, usuario.getStatus());
    }

    @Test
    void shouldMapEntityToResponseDto() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Maria");
        usuario.setTipo(TipoUsuario.PROFESSOR);
        usuario.setEmail("maria@example.com");
        usuario.setTelefone("83988888888");
        usuario.setStatus(StatusUsuario.ATIVO);

        UsuarioResponseDTO dto = mapper.toResponseDTO(usuario);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Maria", dto.getNome());
        assertEquals(TipoUsuario.PROFESSOR, dto.getTipo());
        assertEquals("maria@example.com", dto.getEmail());
        assertEquals("83988888888", dto.getTelefone());
        assertEquals(StatusUsuario.ATIVO, dto.getStatus());
        assertEquals(usuario.getDataCadastro(), dto.getDataCadastro());
    }

    @Test
    void shouldUpdateOnlyProvidedFields() {
        Usuario usuario = new Usuario("Maria", TipoUsuario.PROFESSOR, "maria@example.com", "83988888888");
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome("Maria Silva");
        dto.setTelefone(null);

        mapper.updateEntityFromDTO(dto, usuario);

        assertEquals("Maria Silva", usuario.getNome());
        assertEquals("maria@example.com", usuario.getEmail());
        assertEquals("83988888888", usuario.getTelefone());
    }
}
