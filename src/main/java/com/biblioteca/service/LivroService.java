package com.biblioteca.service;

import com.biblioteca.dto.livro.LivroDTO;
import com.biblioteca.dto.livro.LivroResponseDTO;
import com.biblioteca.mapper.LivroMapper;
import com.biblioteca.model.Livro;
import com.biblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LivroMapper livroMapper;

    @Transactional
    public LivroResponseDTO salvar(LivroDTO livroDTO) {
        Livro livro = livroMapper.toEntity(livroDTO);
        Livro livroSalvo = livroRepository.save(livro);
        return livroMapper.toResponseDTO(livroSalvo);
    }

    public List<LivroResponseDTO> listarTodos() {
        return livroRepository.findAll().stream()
                .map(livroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<LivroResponseDTO> buscarPorId(Long id) {
        return livroRepository.findById(id)
                .map(livroMapper::toResponseDTO);
    }

    public List<LivroResponseDTO> buscarPorTitulo(String titulo) {
        return livroRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(livroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LivroResponseDTO> buscarPorGenero(String genero) {
        return livroRepository.findByGeneroContainingIgnoreCase(genero).stream()
                .map(livroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LivroResponseDTO> buscarPorEditora(String editora) {
        return livroRepository.findByEditoraContainingIgnoreCase(editora).stream()
                .map(livroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LivroResponseDTO atualizar(Long id, LivroDTO livroDTO) {
        return livroRepository.findById(id)
                .map(livro -> {
                    livroMapper.updateEntityFromDTO(livroDTO, livro);
                    Livro livroAtualizado = livroRepository.save(livro);
                    return livroMapper.toResponseDTO(livroAtualizado);
                })
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + id));
    }

    @Transactional
    public void deletar(Long id) {
        if (livroRepository.existsById(id)) {
            livroRepository.deleteById(id);
        } else {
            throw new RuntimeException("Livro não encontrado com ID: " + id);
        }
    }
}