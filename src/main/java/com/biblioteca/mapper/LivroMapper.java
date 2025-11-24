package com.biblioteca.mapper;

import com.biblioteca.dto.livro.LivroRequestDTO;
import com.biblioteca.dto.livro.LivroResponseDTO;
import com.biblioteca.model.Livro;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    Livro toEntity(LivroRequestDTO dto);

    LivroResponseDTO toResponse(Livro entity);
}
