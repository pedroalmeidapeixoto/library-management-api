package com.biblioteca.controller;

import com.biblioteca.dto.livro.LivroRequestDTO;
import com.biblioteca.dto.livro.LivroResponseDTO;
import com.biblioteca.mapper.LivroMapper;
import com.biblioteca.model.Livro;
import com.biblioteca.service.LivroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;
    private final LivroMapper livroMapper;

    public LivroController(LivroService livroService, LivroMapper livroMapper) {
        this.livroService = livroService;
        this.livroMapper = livroMapper;
    }

    @PostMapping
    public ResponseEntity<LivroResponseDTO> criar(@RequestBody LivroRequestDTO dto) {
        Livro livro = livroMapper.toEntity(dto);
        Livro salvo = livroService.criar(livro);
        return ResponseEntity.status(201).body(livroMapper.toResponse(salvo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                livroMapper.toResponse(livroService.buscarPorId(id))
        );
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listar() {
        return ResponseEntity.ok(
                livroService.listar()
                        .stream()
                        .map(livroMapper::toResponse)
                        .toList()
        );
    }
}
