package com.biblioteca.dto.auditoria;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AuditoriaResponseDTO {

    private Long id;

    private Long idEmprestimo;

    private String acao; // EX: "EMPRESTIMO_REALIZADO", "DEVOLUCAO_REALIZADA"

    private LocalDateTime dataAcao;
}
