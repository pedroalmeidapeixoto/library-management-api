package com.biblioteca.mapper;

import com.biblioteca.dto.emprestimo.EmprestimoResponseDTO;
import com.biblioteca.model.Emprestimo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmprestimoMapper {

    @Mapping(source = "usuario.id", target = "idUsuario")
    @Mapping(source = "exemplar.id", target = "idExemplar")
    EmprestimoResponseDTO toResponse(Emprestimo entity);
}
