package com.biblioteca.mapper;

import com.biblioteca.dto.reserva.ReservaResponseDTO;
import com.biblioteca.model.Reserva;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ReservaMapper {

    @Mapping(source = "usuario.id", target = "idUsuario")
    @Mapping(source = "exemplar.id", target = "idExemplar")
    ReservaResponseDTO toResponse(Reserva entity);
}
