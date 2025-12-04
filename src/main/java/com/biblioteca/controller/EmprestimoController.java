package com.biblioteca.controller;

import com.biblioteca.model.Emprestimo;
import com.biblioteca.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    // ✅ POST - Cria empréstimo (ATIVA TRIGGERS!)
    @PostMapping
    public ResponseEntity<?> criarEmprestimo(
            @RequestParam Long usuarioId,
            @RequestParam Long exemplarId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataPrevistaDevolucao) {

        try {
            Emprestimo emprestimo = emprestimoService.criarEmprestimo(usuarioId, exemplarId, dataPrevistaDevolucao);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Empréstimo criado com sucesso!");
            response.put("emprestimoId", emprestimo.getIdEmprestimo());
            response.put("triggerInfo", "Triggers ativadas: trg_validar_emprestimo e trg_auditoria_emprestimo");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao criar empréstimo: " + e.getMessage());
            error.put("triggerInfo", "Possível erro da trigger trg_validar_emprestimo");
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<List<Emprestimo>> listarTodos() {
        return ResponseEntity.ok(emprestimoService.listarTodos());
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(emprestimoService.buscarPorId(id));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ✅ PUT - Atualiza (ATIVA TRIGGER DE AUDITORIA!)
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEmprestimo(
            @PathVariable Long id,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataDevolucaoReal) {

        try {
            Emprestimo emprestimo = emprestimoService.atualizarEmprestimo(id, status, dataDevolucaoReal);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Empréstimo atualizado com sucesso!");
            response.put("emprestimoId", emprestimo.getIdEmprestimo());
            response.put("triggerInfo", "Trigger ativada: trg_auditoria_emprestimo (UPDATE)");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erro ao atualizar empréstimo: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEmprestimo(@PathVariable Long id) {
        try {
            emprestimoService.deletarEmprestimo(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Empréstimo deletado com sucesso!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // ✅ GET por usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Emprestimo>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(emprestimoService.buscarPorUsuario(usuarioId));
    }

    // ✅ GET por status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Emprestimo>> buscarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(emprestimoService.buscarPorStatus(status));
    }
}