package com.biblioteca.controller;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.service.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @PostMapping("/realizar")
    public Emprestimo realizar(
            @RequestParam Long idUsuario,
            @RequestParam Long idExemplar
    ) {
        return emprestimoService.realizarEmprestimo(idUsuario, idExemplar);
    }

    @PostMapping("/{id}/devolver")
    public Emprestimo devolver(@PathVariable Long id) {
        return emprestimoService.finalizarEmprestimo(id);
    }

    @GetMapping("/{id}")
    public Emprestimo buscar(@PathVariable Long id) {
        return emprestimoService.buscarPorId(id);
    }
}
