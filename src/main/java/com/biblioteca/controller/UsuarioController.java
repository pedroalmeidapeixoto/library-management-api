package com.biblioteca.controller;

import com.biblioteca.dto.usuario.UsuarioDTO;
import com.biblioteca.dto.usuario.UsuarioResponseDTO;
import com.biblioteca.model.enums.StatusUsuario;
import com.biblioteca.model.enums.TipoUsuario;
import com.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "http://localhost:3000")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioResponseDTO usuarioCriado = usuarioService.salvar(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorEmail(@PathVariable String email) {
        return usuarioService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarUsuariosPorNome(@PathVariable String nome) {
        List<UsuarioResponseDTO> usuarios = usuarioService.buscarPorNome(nome);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarUsuariosPorTipo(@PathVariable TipoUsuario tipo) {
        List<UsuarioResponseDTO> usuarios = usuarioService.buscarPorTipo(tipo);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarUsuariosPorStatus(@PathVariable StatusUsuario status) {
        List<UsuarioResponseDTO> usuarios = usuarioService.buscarPorStatus(status);
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            UsuarioResponseDTO usuarioAtualizado = usuarioService.atualizar(id, usuarioDTO);
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("Email já cadastrado")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        try {
            usuarioService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/verificar-email/{email}")
    public ResponseEntity<Boolean> verificarEmailExistente(@PathVariable String email) {
        boolean existe = usuarioService.existePorEmail(email);
        return ResponseEntity.ok(existe);
    }
}