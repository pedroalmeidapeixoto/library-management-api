package com.biblioteca.service;

import com.biblioteca.exception.NotFoundException;
import com.biblioteca.model.Livro;
import com.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;

    // Criar
    public Livro criarLivro(Livro livro) {
        return livroRepository.save(livro);
    }

    // Buscar por ID
    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado"));
    }

    // Listar
    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    // Atualizar
    public Livro atualizar(Livro livroAtualizado) {

        Livro livro = livroRepository.findById(livroAtualizado.getId())
                .orElseThrow(() -> new NotFoundException("Livro não encontrado"));

        livro.setTitulo(livroAtualizado.getTitulo());
        livro.setAnoPublicacao(livroAtualizado.getAnoPublicacao());
        livro.setEditora(livroAtualizado.getEditora());
        livro.setGenero(livroAtualizado.getGenero());

        return livroRepository.save(livro);
    }

    // Remover
    public void remover(Long id) {

        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro não encontrado"));

        livroRepository.delete(livro);
    }
}
