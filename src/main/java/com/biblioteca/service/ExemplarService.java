package com.biblioteca.service;

import com.biblioteca.model.Exemplar;
import com.biblioteca.repository.ExemplarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExemplarService {

    @Autowired
    private ExemplarRepository exemplarRepository;

    // CREATE
    @Transactional
    public Exemplar criarExemplar(Exemplar exemplar) {
        return exemplarRepository.save(exemplar);
    }

    // READ ALL
    public List<Exemplar> listarTodos() {
        return exemplarRepository.findAll();
    }

    // READ BY ID
    public Exemplar buscarPorId(Long id) {
        return exemplarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exemplar não encontrado ID: " + id));
    }

    // UPDATE
    @Transactional
    public Exemplar atualizarExemplar(Long id, String status, String localizacao) {
        Exemplar exemplar = buscarPorId(id);

        if (status != null) {
            exemplar.setStatus(status);
        }

        if (localizacao != null) {
            exemplar.setLocalizacao(localizacao);
        }

        return exemplarRepository.save(exemplar);
    }

    // DELETE
    @Transactional
    public void deletarExemplar(Long id) {
        if (!exemplarRepository.existsById(id)) {
            throw new RuntimeException("Exemplar não encontrado ID: " + id);
        }
        exemplarRepository.deleteById(id);
    }

    // Buscar disponíveis
    public List<Exemplar> buscarDisponiveis() {
        return exemplarRepository.findByStatus("disponivel");
    }
}