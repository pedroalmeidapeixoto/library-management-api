package com.biblioteca.mapper;

import com.biblioteca.dto.exemplar.ExemplarRequestDTO;
import com.biblioteca.dto.exemplar.ExemplarResponseDTO;
import com.biblioteca.model.Exemplar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ExemplarMapper {

    @Mapping(source = "idLivro", target = "livro.id")
    Exemplar toEntity(ExemplarRequestDTO dto);

    @Mapping(source = "livro.id", target = "idLivro")
    ExemplarResponseDTO toResponse(Exemplar entity);
}
