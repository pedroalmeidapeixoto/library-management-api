package com.biblioteca.service;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.ExemplarRepository;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ExemplarRepository exemplarRepository;

    // ✅ CREATE - Vai ATIVAR AS TRIGGERS automaticamente!
    @Transactional
    public Emprestimo criarEmprestimo(Long usuarioId, Long exemplarId, LocalDate dataPrevistaDevolucao) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ID: " + usuarioId));

        Exemplar exemplar = exemplarRepository.findById(exemplarId)
                .orElseThrow(() -> new RuntimeException("Exemplar não encontrado ID: " + exemplarId));

        Emprestimo emprestimo = new Emprestimo(usuario, exemplar, dataPrevistaDevolucao);

        // 🚨 ESTA LINHA ATIVA AS TRIGGERS:
        // 1. BEFORE INSERT: trg_validar_emprestimo (valida se exemplar está 'disponivel')
        // 2. AFTER INSERT: trg_auditoria_emprestimo (registra na tabela auditoria_emprestimo)
        return emprestimoRepository.save(emprestimo);
    }

    // ✅ READ ALL
    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAll();
    }

    // ✅ READ BY ID
    public Emprestimo buscarPorId(Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado ID: " + id));
    }

    // ✅ UPDATE - Também ativa trigger de auditoria!
    @Transactional
    public Emprestimo atualizarEmprestimo(Long id, String novoStatus, LocalDate dataDevolucaoReal) {
        Emprestimo emprestimo = buscarPorId(id);

        if (novoStatus != null) {
            emprestimo.setStatus(novoStatus);
        }

        if (dataDevolucaoReal != null) {
            emprestimo.setDataDevolucaoReal(dataDevolucaoReal);
        }

        // 🚨 UPDATE também ativa: trg_auditoria_emprestimo (AFTER UPDATE)
        return emprestimoRepository.save(emprestimo);
    }

    // ✅ DELETE
    @Transactional
    public void deletarEmprestimo(Long id) {
        if (!emprestimoRepository.existsById(id)) {
            throw new RuntimeException("Empréstimo não encontrado ID: " + id);
        }
        emprestimoRepository.deleteById(id);
    }

    // Métodos auxiliares
    public List<Emprestimo> buscarPorUsuario(Long usuarioId) {
        return emprestimoRepository.findByUsuario_Id(usuarioId);
    }

    public List<Emprestimo> buscarPorStatus(String status) {
        return emprestimoRepository.findByStatus(status);
    }
}