package com.biblioteca.controller;

import com.biblioteca.model.Exemplar;
import com.biblioteca.service.ExemplarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exemplares")
public class ExemplarController {

    @Autowired
    private ExemplarService exemplarService;

    // POST
    @PostMapping
    public ResponseEntity<?> criarExemplar(@RequestBody Exemplar exemplar) {
        try {
            Exemplar novo = exemplarService.criarExemplar(exemplar);
            return ResponseEntity.status(HttpStatus.CREATED).body(novo);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Exemplar>> listarTodos() {
        return ResponseEntity.ok(exemplarService.listarTodos());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(exemplarService.buscarPorId(id));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarExemplar(
            @PathVariable Long id,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String localizacao) {

        try {
            Exemplar exemplar = exemplarService.atualizarExemplar(id, status, localizacao);
            return ResponseEntity.ok(exemplar);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarExemplar(@PathVariable Long id) {
        try {
            exemplarService.deletarExemplar(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Exemplar deletado com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // GET disponíveis
    @GetMapping("/disponiveis")
    public ResponseEntity<List<Exemplar>> listarDisponiveis() {
        return ResponseEntity.ok(exemplarService.buscarDisponiveis());
    }
}