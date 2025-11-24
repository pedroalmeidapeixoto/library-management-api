package com.biblioteca.controller;

import com.biblioteca.model.AuditoriaEmprestimo;
import com.biblioteca.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public List<AuditoriaEmprestimo> listar() {
        return auditoriaService.listarTodos();
    }
}
