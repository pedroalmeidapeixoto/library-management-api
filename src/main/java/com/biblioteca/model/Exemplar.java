package com.biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "exemplar")
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exemplar")
    private Long idExemplar;

    @Column(name = "id_livro", nullable = false)
    private Integer idLivro;

    @Column(length = 20)
    private String status = "disponivel";

    @Column(length = 50)
    private String localizacao;

    @Column(name = "data_aquisicao")
    private LocalDate dataAquisicao = LocalDate.now();

    // Construtor
    public Exemplar() {}

    public Exemplar(Integer idLivro, String localizacao) {
        this.idLivro = idLivro;
        this.localizacao = localizacao;
    }

    // Getters e Setters
    public Long getIdExemplar() { return idExemplar; }
    public void setIdExemplar(Long idExemplar) { this.idExemplar = idExemplar; }

    public Integer getIdLivro() { return idLivro; }
    public void setIdLivro(Integer idLivro) { this.idLivro = idLivro; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public LocalDate getDataAquisicao() { return dataAquisicao; }
    public void setDataAquisicao(LocalDate dataAquisicao) { this.dataAquisicao = dataAquisicao; }
}