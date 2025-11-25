package com.biblioteca.mapper;

import com.biblioteca.dto.request.ReservaRequestDTO;
import com.biblioteca.dto.response.ReservaResponseDTO;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Reserva;
import com.biblioteca.model.Usuario;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservaMapperTest {

    private final ReservaMapper mapper = Mappers.getMapper(ReservaMapper.class);

    @Test
    void testToEntity() {
        ReservaRequestDTO dto = new ReservaRequestDTO();
        dto.setIdUsuario(1L);
        dto.setIdExemplar(5L);

        Reserva reserva = mapper.toEntity(dto);

        assertNotNull(reserva);
        assertNotNull(reserva.getUsuario());
        assertNotNull(reserva.getExemplar());
        assertEquals(1L, reserva.getUsuario().getId());
        assertEquals(5L, reserva.getExemplar().getId());
    }

    @Test
    void testToResponseDTO() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Exemplar exemplar = new Exemplar();
        exemplar.setId(10L);

        Reserva reserva = new Reserva();
        reserva.setId(99L);
        reserva.setUsuario(usuario);
        reserva.setExemplar(exemplar);
        reserva.setDataReserva(LocalDate.of(2025, 1, 1));

        ReservaResponseDTO dto = mapper.toResponseDTO(reserva);

        assertNotNull(dto);
        assertEquals(99L, dto.getId());
        assertEquals(1L, dto.getIdUsuario());
        assertEquals(10L, dto.getIdExemplar());
        assertEquals(LocalDate.of(2025, 1, 1), dto.getDataReserva());
    }
}
