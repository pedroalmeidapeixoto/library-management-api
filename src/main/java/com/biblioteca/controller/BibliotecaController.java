package com.biblioteca.controller;

import com.biblioteca.dto.devolucao.DevolucaoResponse;
import com.biblioteca.dto.devolucao.TotalMultaResponse;
import com.biblioteca.dto.devolucao.DisponibilidadeResponse;
import com.biblioteca.service.DevolucaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BibliotecaController {

    @Autowired
    private DevolucaoService devolucaoService;

    // Endpoint 1: Chama a PROCEDURE de devolução
    @PostMapping("/emprestimos/{id}/devolver")
    public ResponseEntity<DevolucaoResponse> realizarDevolucao(
            @PathVariable("id") Integer idEmprestimo) {

        try {
            devolucaoService.realizarDevolucao(idEmprestimo);

            DevolucaoResponse response = new DevolucaoResponse(
                    "Devolução realizada com sucesso.",
                    idEmprestimo
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            DevolucaoResponse errorResponse = new DevolucaoResponse(
                    "Erro na devolução: " + e.getMessage(),
                    idEmprestimo
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Endpoint 2: Usa a FUNCTION fn_multas_usuario para dados completos
    @GetMapping("/usuarios/{id}/multas")
    public ResponseEntity<Map<String, Object>> getMultasUsuario(
            @PathVariable("id") Integer idUsuario) {

        try {
            Map<String, Object> result = devolucaoService.getMultasUsuario(idUsuario);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Endpoint 3: Usa a FUNCTION fn_exemplar_disponivel
    @GetMapping("/exemplares/{id}/disponivel")
    public ResponseEntity<DisponibilidadeResponse> verificarDisponibilidade(
            @PathVariable("id") Integer idExemplar) {

        try {
            Boolean disponivel = devolucaoService.verificarDisponibilidade(idExemplar);

            DisponibilidadeResponse response = new DisponibilidadeResponse(
                    idExemplar,
                    disponivel,
                    disponivel ? "Exemplar disponível para empréstimo" : "Exemplar não disponível"
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            DisponibilidadeResponse errorResponse = new DisponibilidadeResponse(
                    idExemplar,
                    false,
                    "Erro: " + e.getMessage()
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    // No seu BibliotecaController, adicione:
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("✅ API Biblioteca está funcionando! - " +
                System.currentTimeMillis());
    }

}