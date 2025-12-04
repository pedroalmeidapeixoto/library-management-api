package com.biblioteca.repository;

import com.biblioteca.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    List<Emprestimo> findByUsuario_Id(Long usuarioId);
    List<Emprestimo> findByExemplar_IdExemplar(Long idExemplar);
    List<Emprestimo> findByStatus(String status);
}