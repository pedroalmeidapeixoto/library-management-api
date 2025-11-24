package com.biblioteca.mapper;

import com.biblioteca.dto.auditoria.AuditoriaResponseDTO;
import com.biblioteca.model.AuditoriaEmprestimo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuditoriaMapper {

    @Mapping(source = "emprestimo.id", target = "idEmprestimo")
    AuditoriaResponseDTO toResponse(AuditoriaEmprestimo entity);
}
