package com.biblioteca.dto.livro;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class LivroResponseDTO {

    private Long id;
    private String titulo;
    private Integer anoPublicacao;
    private String editora;
    private String genero;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCadastro;

    // Construtores
    public LivroResponseDTO() {}

    public LivroResponseDTO(Long id, String titulo, Integer anoPublicacao,
                            String editora, String genero, LocalDateTime dataCadastro) {
        this.id = id;
        this.titulo = titulo;
        this.anoPublicacao = anoPublicacao;
        this.editora = editora;
        this.genero = genero;
        this.dataCadastro = dataCadastro;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}