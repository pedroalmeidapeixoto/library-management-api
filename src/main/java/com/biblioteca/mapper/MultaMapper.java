package com.biblioteca.mapper;

import com.biblioteca.dto.multa.MultaResponseDTO;
import com.biblioteca.model.Multa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MultaMapper {

    @Mapping(source = "emprestimo.id", target = "idEmprestimo")
    MultaResponseDTO toResponse(Multa entity);
}
