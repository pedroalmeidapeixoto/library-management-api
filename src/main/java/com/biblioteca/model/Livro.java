package com.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "livro")
@Data
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livro")
    private Long id;

    @Column(nullable = false, length = 250)
    private String titulo;

    @Column(name = "ano_publicacao")
    private Integer anoPublicacao;

    private String editora;

    private String genero;
}
