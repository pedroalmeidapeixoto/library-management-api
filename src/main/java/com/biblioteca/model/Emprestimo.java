package com.biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "emprestimo")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emprestimo")
    private Long idEmprestimo;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_exemplar", nullable = false)
    private Exemplar exemplar;

    @Column(name = "data_emprestimo")
    private LocalDate dataEmprestimo = LocalDate.now();

    @Column(name = "data_prevista_devolucao", nullable = false)
    private LocalDate dataPrevistaDevolucao;

    @Column(name = "data_devolucao_real")
    private LocalDate dataDevolucaoReal;

    @Column(length = 20)
    private String status = "pendente";

    @Column
    private String observacao;

    // Construtor
    public Emprestimo() {}

    public Emprestimo(Usuario usuario, Exemplar exemplar, LocalDate dataPrevistaDevolucao) {
        this.usuario = usuario;
        this.exemplar = exemplar;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    // Getters e Setters
    public Long getIdEmprestimo() { return idEmprestimo; }
    public void setIdEmprestimo(Long idEmprestimo) { this.idEmprestimo = idEmprestimo; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Exemplar getExemplar() { return exemplar; }
    public void setExemplar(Exemplar exemplar) { this.exemplar = exemplar; }

    public LocalDate getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDate dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }

    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) { this.dataPrevistaDevolucao = dataPrevistaDevolucao; }

    public LocalDate getDataDevolucaoReal() { return dataDevolucaoReal; }
    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) { this.dataDevolucaoReal = dataDevolucaoReal; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}