package com.biblioteca.controller;

import com.biblioteca.dto.auditoria.AuditoriaResponseDTO;
import com.biblioteca.mapper.AuditoriaMapper;
import com.biblioteca.service.AuditoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditorias")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;
    private final AuditoriaMapper auditoriaMapper;

    public AuditoriaController(AuditoriaService auditoriaService, AuditoriaMapper auditoriaMapper) {
        this.auditoriaService = auditoriaService;
        this.auditoriaMapper = auditoriaMapper;
    }

    @GetMapping
    public ResponseEntity<List<AuditoriaResponseDTO>> listar() {
        return ResponseEntity.ok(
                auditoriaService.listar()
                        .stream()
                        .map(auditoriaMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                auditoriaMapper.toResponse(auditoriaService.buscarPorId(id))
        );
    }
}
