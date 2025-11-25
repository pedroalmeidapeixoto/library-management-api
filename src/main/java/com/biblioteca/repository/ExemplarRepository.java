package com.biblioteca.repository;

import com.biblioteca.model.Exemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExemplarRepository extends JpaRepository<Exemplar, Long> {

    // Lista todos os exemplares de um livro específico
    List<Exemplar> findByLivroId(Long livroId);
}
