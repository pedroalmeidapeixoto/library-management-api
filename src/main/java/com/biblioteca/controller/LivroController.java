package com.biblioteca.controller;

import com.biblioteca.dto.livro.LivroDTO;
import com.biblioteca.dto.livro.LivroResponseDTO;
import com.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/livros")
@CrossOrigin(origins = "http://localhost:3000")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @PostMapping
    public ResponseEntity<LivroResponseDTO> criarLivro(@Valid @RequestBody LivroDTO livroDTO) {
        LivroResponseDTO livroSalvo = livroService.salvar(livroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listarLivros() {
        List<LivroResponseDTO> livros = livroService.listarTodos();
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarLivroPorId(@PathVariable Long id) {
        return livroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<LivroResponseDTO>> buscarLivrosPorTitulo(@PathVariable String titulo) {
        List<LivroResponseDTO> livros = livroService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<LivroResponseDTO>> buscarLivrosPorGenero(@PathVariable String genero) {
        List<LivroResponseDTO> livros = livroService.buscarPorGenero(genero);
        return ResponseEntity.ok(livros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizarLivro(
            @PathVariable Long id,
            @Valid @RequestBody LivroDTO livroDTO) {
        try {
            LivroResponseDTO livroAtualizado = livroService.atualizar(id, livroDTO);
            return ResponseEntity.ok(livroAtualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        try {
            livroService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}