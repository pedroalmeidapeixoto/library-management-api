package com.biblioteca.service;

import com.biblioteca.exception.NotFoundException;
import com.biblioteca.model.AuditoriaEmprestimo;
import com.biblioteca.repository.AuditoriaEmprestimoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaEmprestimoRepository auditoriaRepository;

    // -------------------------------------------------------
    // Listar todas as auditorias (Controller chama listar)
    // -------------------------------------------------------
    public List<AuditoriaEmprestimo> listar() {
        return auditoriaRepository.findAll();
    }

    // -------------------------------------------------------
    // Buscar auditoria por ID
    // -------------------------------------------------------
    public AuditoriaEmprestimo buscarPorId(Long id) {
        return auditoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Auditoria não encontrada"));
    }
}
