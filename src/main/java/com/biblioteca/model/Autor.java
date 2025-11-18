package com.biblioteca.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "autor")
@Data
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_autor")
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;
}
