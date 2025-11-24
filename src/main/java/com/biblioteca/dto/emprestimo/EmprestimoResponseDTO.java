package com.biblioteca.dto.emprestimo;

import com.biblioteca.model.enums.StatusEmprestimo;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmprestimoResponseDTO {

    private Long id;
    private Long idUsuario;
    private Long idExemplar;

    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;

    private StatusEmprestimo status;
}
