package com.biblioteca.service;

import com.biblioteca.model.AuditoriaEmprestimo;
import com.biblioteca.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    // -------------------------------------------------------
    // Listar toda auditoria
    // -------------------------------------------------------
    public List<AuditoriaEmprestimo> listarTodos() {
        return auditoriaRepository.findAll();
    }
}
