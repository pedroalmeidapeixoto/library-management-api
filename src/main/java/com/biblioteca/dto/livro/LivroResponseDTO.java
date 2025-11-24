package com.biblioteca.dto.livro;

import lombok.Data;

@Data
public class LivroResponseDTO {
    private Long id;
    private String titulo;
    private String autor;
    private String categoria;
    private Integer anoPublicacao;
}
