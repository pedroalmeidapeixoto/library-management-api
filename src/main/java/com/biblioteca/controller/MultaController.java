package com.biblioteca.controller;

import com.biblioteca.model.Multa;
import com.biblioteca.service.MultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/multas")
@RequiredArgsConstructor
public class MultaController {

    private final MultaService multaService;

    @PostMapping("/{id}/pagar")
    public Multa pagar(@PathVariable Long id) {
        return multaService.marcarComoPaga(id);
    }

    @GetMapping("/pendentes")
    public List<Multa> pendentes() {
        return multaService.listarMultasPendentes();
    }

    @GetMapping
    public List<Multa> listar() {
        return multaService.listarTodas();
    }
}
