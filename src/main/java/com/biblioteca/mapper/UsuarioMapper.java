package com.biblioteca.mapper;

import com.biblioteca.dto.usuario.UsuarioRequestDTO;
import com.biblioteca.dto.usuario.UsuarioResponseDTO;
import com.biblioteca.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioRequestDTO dto);

    UsuarioResponseDTO toResponse(Usuario entity);
}
