package com.biblioteca.mapper;

import com.biblioteca.dto.response.EmprestimoResponseDTO;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Usuario;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EmprestimoMapperTest {

    private final EmprestimoMapper mapper = Mappers.getMapper(EmprestimoMapper.class);

    @Test
    void testToResponseDTO() {
        Usuario usuario = new Usuario();
        usuario.setId(3L);

        Exemplar exemplar = new Exemplar();
        exemplar.setId(7L);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(100L);
        emprestimo.setUsuario(usuario);
        emprestimo.setExemplar(exemplar);
        emprestimo.setDataEmprestimo(LocalDate.of(2025, 1, 10));
        emprestimo.setDataDevolucao(null);

        EmprestimoResponseDTO dto = mapper.toResponseDTO(emprestimo);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(3L, dto.getIdUsuario());
        assertEquals(7L, dto.getIdExemplar());
        assertEquals(LocalDate.of(2025, 1, 10), dto.getDataEmprestimo());
        assertNull(dto.getDataDevolucao());
    }
}
