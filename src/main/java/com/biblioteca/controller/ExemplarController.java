package com.biblioteca.controller;

import com.biblioteca.model.Exemplar;
import com.biblioteca.model.enums.StatusExemplar;
import com.biblioteca.service.ExemplarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exemplares")
@RequiredArgsConstructor
public class ExemplarController {

    private final ExemplarService exemplarService;

    @PostMapping
    public Exemplar criar(@RequestBody Exemplar exemplar) {
        return exemplarService.criarExemplar(exemplar);
    }

    @GetMapping("/{id}")
    public Exemplar buscar(@PathVariable Long id) {
        return exemplarService.buscarPorId(id);
    }

    @GetMapping("/livro/{idLivro}")
    public List<Exemplar> listarPorLivro(@PathVariable Long idLivro) {
        return exemplarService.listarPorLivro(idLivro);
    }

    @GetMapping("/{id}/disponivel")
    public boolean estaDisponivel(@PathVariable Long id) {
        return exemplarService.estaDisponivel(id);
    }

    @PutMapping("/{id}/status")
    public Exemplar atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusExemplar status
    ) {
        return exemplarService.atualizarStatus(id, status);
    }
}
