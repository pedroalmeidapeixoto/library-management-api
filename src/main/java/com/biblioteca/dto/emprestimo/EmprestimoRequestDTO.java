package com.biblioteca.dto.emprestimo;

import lombok.Data;

@Data
public class EmprestimoRequestDTO {
    private Long idUsuario;
    private Long idExemplar;
}
