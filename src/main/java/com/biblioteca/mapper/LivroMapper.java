package com.biblioteca.mapper;

import com.biblioteca.dto.livro.LivroRequestDTO;
import com.biblioteca.dto.livro.LivroResponseDTO;
import com.biblioteca.model.Livro;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LivroMapper {

    Livro toEntity(LivroRequestDTO dto);

    LivroResponseDTO toResponse(Livro entity);
}
