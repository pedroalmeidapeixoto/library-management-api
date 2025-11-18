package com.biblioteca.repository;

import com.biblioteca.model.LivroAutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroAutorRepository extends JpaRepository<LivroAutor, Long> {
}
