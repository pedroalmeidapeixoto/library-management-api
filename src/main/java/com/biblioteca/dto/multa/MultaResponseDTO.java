package com.biblioteca.dto.multa;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MultaResponseDTO {
    private Long id;
    private Long idEmprestimo;
    private double valor;
    private boolean paga;
    private LocalDate dataGeracao;
}
