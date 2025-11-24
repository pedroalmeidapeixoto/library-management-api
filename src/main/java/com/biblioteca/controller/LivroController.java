package com.biblioteca.controller;

import com.biblioteca.model.Livro;
import com.biblioteca.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    public Livro criar(@RequestBody Livro livro) {
        return livroService.criarLivro(livro);
    }

    @GetMapping("/{id}")
    public Livro buscar(@PathVariable Long id) {
        return livroService.buscarPorId(id);
    }

    @GetMapping
    public List<Livro> listar() {
        return livroService.listarTodos();
    }

    @PutMapping("/{id}")
    public Livro atualizar(@PathVariable Long id, @RequestBody Livro livro) {
        livro.setId(id);
        return livroService.atualizar(livro);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        livroService.remover(id);
    }
}
