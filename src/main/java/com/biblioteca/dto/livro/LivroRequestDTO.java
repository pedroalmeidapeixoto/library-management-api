package com.biblioteca.dto.livro;

import lombok.Data;

@Data
public class LivroRequestDTO {
    private String titulo;
    private String autor;
    private String categoria;
    private Integer anoPublicacao;
}
