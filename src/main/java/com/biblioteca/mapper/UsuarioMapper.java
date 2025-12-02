package com.biblioteca.mapper;

import com.biblioteca.dto.usuario.UsuarioDTO;
import com.biblioteca.dto.usuario.UsuarioResponseDTO;
import com.biblioteca.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setTipo(dto.getTipo());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefone(dto.getTelefone());
        usuario.setStatus(dto.getStatus());

        return usuario;
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
        responseDTO.setId(usuario.getId());
        responseDTO.setNome(usuario.getNome());
        responseDTO.setTipo(usuario.getTipo());
        responseDTO.setEmail(usuario.getEmail());
        responseDTO.setTelefone(usuario.getTelefone());
        responseDTO.setDataCadastro(usuario.getDataCadastro());
        responseDTO.setStatus(usuario.getStatus());

        return responseDTO;
    }

    public void updateEntityFromDTO(UsuarioDTO dto, Usuario usuario) {
        if (dto == null || usuario == null) return;

        if (dto.getNome() != null) usuario.setNome(dto.getNome());
        if (dto.getTipo() != null) usuario.setTipo(dto.getTipo());
        if (dto.getEmail() != null) usuario.setEmail(dto.getEmail());
        if (dto.getTelefone() != null) usuario.setTelefone(dto.getTelefone());
        if (dto.getStatus() != null) usuario.setStatus(dto.getStatus());
    }
}