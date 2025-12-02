package com.biblioteca.dto.livro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LivroDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 200, message = "Título deve ter entre 3 e 200 caracteres")
    private String titulo;

    private Integer anoPublicacao;

    @Size(max = 100, message = "Editora deve ter no máximo 100 caracteres")
    private String editora;

    @Size(max = 50, message = "Gênero deve ter no máximo 50 caracteres")
    private String genero;

    // Getters e Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
}