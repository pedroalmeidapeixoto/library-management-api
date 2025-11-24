package com.biblioteca.controller;

import com.biblioteca.model.Usuario;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Multa;
import com.biblioteca.model.Reserva;
import com.biblioteca.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public Usuario criar(@RequestBody Usuario usuario) {
        return usuarioService.criarUsuario(usuario);
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @GetMapping("/{id}/emprestimos")
    public List<Emprestimo> emprestimos(@PathVariable Long id) {
        return usuarioService.listarEmprestimos(id);
    }

    @GetMapping("/{id}/reservas")
    public List<Reserva> reservas(@PathVariable Long id) {
        return usuarioService.listarReservas(id);
    }

    @GetMapping("/{id}/multas")
    public List<Multa> multas(@PathVariable Long id) {
        return usuarioService.listarMultas(id);
    }
}
