package com.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "livro_autor")
@Data
public class LivroAutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livro_autor")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_livro", nullable = false)
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "id_autor", nullable = false)
    private Autor autor;

    @Column(name = "ordem_autor")
    private Integer ordemAutor;
}
