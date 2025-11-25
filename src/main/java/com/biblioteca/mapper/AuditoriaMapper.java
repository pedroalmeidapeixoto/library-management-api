package com.biblioteca.mapper;

import com.biblioteca.dto.auditoria.AuditoriaResponseDTO;
import com.biblioteca.model.AuditoriaEmprestimo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AuditoriaMapper {

    @Mapping(source = "emprestimo.id", target = "idEmprestimo")
    AuditoriaResponseDTO toResponse(AuditoriaEmprestimo entity);
}
