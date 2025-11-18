package com.biblioteca.model;

import com.biblioteca.model.enums.StatusExemplar;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "exemplar")
@Data
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_exemplar")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_livro", nullable = false)
    private Livro livro;

    @Column(name = "codigo_exemplar", unique = true, length = 80)
    private String codigoExemplar;

    @Enumerated(EnumType.STRING)
    private StatusExemplar status;

    @Column(name = "data_aquisicao")
    private LocalDate dataAquisicao;
}
