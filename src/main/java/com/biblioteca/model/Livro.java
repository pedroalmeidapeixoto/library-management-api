package com.biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livro")
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(name = "ano_publicacao")
    private Integer anoPublicacao;

    @Column(length = 100)
    private String editora;

    @Column(length = 50)
    private String genero;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    // Construtores
    public Livro() {
        this.dataCadastro = LocalDateTime.now();
    }

    public Livro(String titulo) {
        this.titulo = titulo;
        this.dataCadastro = LocalDateTime.now();
    }

    public Livro(String titulo, Integer anoPublicacao, String editora, String genero) {
        this.titulo = titulo;
        this.anoPublicacao = anoPublicacao;
        this.editora = editora;
        this.genero = genero;
        this.dataCadastro = LocalDateTime.now();
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