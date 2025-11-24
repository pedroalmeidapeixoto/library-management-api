package com.biblioteca.controller;

import com.biblioteca.dto.multa.MultaResponseDTO;
import com.biblioteca.mapper.MultaMapper;
import com.biblioteca.model.Multa;
import com.biblioteca.service.MultaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/multas")
public class MultaController {

    private final MultaService multaService;
    private final MultaMapper multaMapper;

    public MultaController(MultaService multaService, MultaMapper multaMapper) {
        this.multaService = multaService;
        this.multaMapper = multaMapper;
    }

    @GetMapping
    public ResponseEntity<List<MultaResponseDTO>> listar() {
        return ResponseEntity.ok(
                multaService.listar()
                        .stream()
                        .map(multaMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultaResponseDTO> buscarPorId(@PathVariable Long id) {
        Multa multa = multaService.buscarPorId(id);
        return ResponseEntity.ok(multaMapper.toResponse(multa));
    }
}
