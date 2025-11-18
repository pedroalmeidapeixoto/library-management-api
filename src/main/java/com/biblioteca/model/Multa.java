package com.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "multa")
@Data
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_multa")
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_emprestimo", nullable = false)
    private Emprestimo emprestimo;

    @Column(nullable = false)
    private Double valor;

    @Column(name = "data_aplicacao")
    private LocalDateTime dataAplicacao;

    private Boolean pago;

    private String observacao;
}
