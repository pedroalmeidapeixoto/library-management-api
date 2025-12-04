package com.biblioteca.repository;

import com.biblioteca.model.Exemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExemplarRepository extends JpaRepository<Exemplar, Long> {
    List<Exemplar> findByStatus(String status);
    List<Exemplar> findByIdLivro(Integer idLivro);
}