package com.biblioteca.controller;

import com.biblioteca.dto.emprestimo.EmprestimoResponseDTO;
import com.biblioteca.mapper.EmprestimoMapper;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.service.EmprestimoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;
    private final EmprestimoMapper emprestimoMapper;

    public EmprestimoController(EmprestimoService emprestimoService, EmprestimoMapper emprestimoMapper) {
        this.emprestimoService = emprestimoService;
        this.emprestimoMapper = emprestimoMapper;
    }

    @PostMapping
    public ResponseEntity<EmprestimoResponseDTO> emprestar(
            @RequestParam Long idUsuario,
            @RequestParam Long idExemplar
    ) {
        Emprestimo emp = emprestimoService.realizarEmprestimo(idUsuario, idExemplar);
        return ResponseEntity.status(201).body(emprestimoMapper.toResponse(emp));
    }

    @PostMapping("/devolucao/{id}")
    public ResponseEntity<EmprestimoResponseDTO> devolver(@PathVariable Long id) {
        Emprestimo emp = emprestimoService.realizarDevolucao(id);
        return ResponseEntity.ok(emprestimoMapper.toResponse(emp));
    }

    @GetMapping
    public ResponseEntity<List<EmprestimoResponseDTO>> listar() {
        return ResponseEntity.ok(
                emprestimoService.listar()
                        .stream()
                        .map(emprestimoMapper::toResponse)
                        .toList()
        );
    }
}
