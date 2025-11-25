package com.biblioteca.controller;

import com.biblioteca.dto.exemplar.ExemplarRequestDTO;
import com.biblioteca.dto.exemplar.ExemplarResponseDTO;
import com.biblioteca.mapper.ExemplarMapper;
import com.biblioteca.model.Exemplar;
import com.biblioteca.service.ExemplarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exemplares")
public class ExemplarController {

    private final ExemplarService exemplarService;
    private final ExemplarMapper exemplarMapper;

    public ExemplarController(ExemplarService exemplarService, ExemplarMapper exemplarMapper) {
        this.exemplarService = exemplarService;
        this.exemplarMapper = exemplarMapper;
    }

    // -------------------------------------------------------
    // Criar exemplar
    // -------------------------------------------------------
    @PostMapping
    public ResponseEntity<ExemplarResponseDTO> criar(@RequestBody ExemplarRequestDTO dto) {
        Exemplar exemplar = exemplarMapper.toEntity(dto);
        Exemplar salvo = exemplarService.criar(exemplar, dto.getIdLivro());
        return ResponseEntity.status(201).body(exemplarMapper.toResponse(salvo));
    }

    // -------------------------------------------------------
    // Buscar exemplar por ID
    // -------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ExemplarResponseDTO> buscarPorId(@PathVariable Long id) {
        Exemplar exemplar = exemplarService.buscarPorId(id);
        return ResponseEntity.ok(exemplarMapper.toResponse(exemplar));
    }

    // -------------------------------------------------------
    // Listar todos os exemplares
    // -------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<ExemplarResponseDTO>> listar() {
        List<Exemplar> exemplares = exemplarService.listar();
        List<ExemplarResponseDTO> response = exemplares.stream()
                .map(exemplarMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }
}
