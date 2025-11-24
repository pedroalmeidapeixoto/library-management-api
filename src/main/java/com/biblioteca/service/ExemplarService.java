package com.biblioteca.service;

import com.biblioteca.exception.BusinessException;
import com.biblioteca.exception.NotFoundException;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Livro;
import com.biblioteca.model.enums.StatusExemplar;
import com.biblioteca.repository.ExemplarRepository;
import com.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExemplarService {

    private final ExemplarRepository exemplarRepository;
    private final LivroRepository livroRepository;

    // -------------------------------------------------------
    // Criar exemplar
    // -------------------------------------------------------
    public Exemplar criarExemplar(Exemplar exemplar) {

        Long idLivro = exemplar.getLivro().getId();

        Livro livro = livroRepository.findById(idLivro)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado"));

        exemplar.setLivro(livro);
        exemplar.setStatus(StatusExemplar.DISPONIVEL);

        return exemplarRepository.save(exemplar);
    }

    // -------------------------------------------------------
    // Buscar exemplar
    // -------------------------------------------------------
    public Exemplar buscarPorId(Long id) {
        return exemplarRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Exemplar não encontrado"));
    }

    // -------------------------------------------------------
    // Listar por livro
    // -------------------------------------------------------
    public List<Exemplar> listarPorLivro(Long idLivro) {
        return exemplarRepository.findByLivroId(idLivro);
    }

    // -------------------------------------------------------
    // Verificar disponibilidade
    // -------------------------------------------------------
    public boolean estaDisponivel(Long id) {
        Exemplar ex = buscarPorId(id);
        return ex.getStatus() == StatusExemplar.DISPONIVEL;
    }

    // -------------------------------------------------------
    // Atualizar status
    // -------------------------------------------------------
    public Exemplar atualizarStatus(Long id, StatusExemplar status) {

        if (status == null) {
            throw new BusinessException("Status inválido");
        }

        Exemplar ex = buscarPorId(id);
        ex.setStatus(status);

        return exemplarRepository.save(ex);
    }
}
