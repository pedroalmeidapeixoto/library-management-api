package com.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_emprestimo")
@Data
public class AuditoriaEmprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_emprestimo", nullable = false)
    private Emprestimo emprestimo;

    private String evento;

    @Column(name = "usuario_operacao")
    private String usuarioOperacao;

    @Column(name = "data_evento")
    private LocalDateTime dataEvento;

    private String detalhe;
}
