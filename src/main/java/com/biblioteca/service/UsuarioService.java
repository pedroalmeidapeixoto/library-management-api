package com.biblioteca.service;

import com.biblioteca.exception.NotFoundException;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Multa;
import com.biblioteca.model.Reserva;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.MultaRepository;
import com.biblioteca.repository.ReservaRepository;
import com.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final ReservaRepository reservaRepository;
    private final MultaRepository multaRepository;

    // -------------------------------------------------------
    // Criar usuário
    // -------------------------------------------------------
    public Usuario criarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // -------------------------------------------------------
    // Buscar usuário
    // -------------------------------------------------------
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    // -------------------------------------------------------
    // Listar empréstimos do usuário
    // -------------------------------------------------------
    public List<Emprestimo> listarEmprestimos(Long idUsuario) {
        buscarPorId(idUsuario); // valida existência
        return emprestimoRepository.findByUsuarioId(idUsuario);
    }

    // -------------------------------------------------------
    // Listar reservas do usuário
    // -------------------------------------------------------
    public List<Reserva> listarReservas(Long idUsuario) {
        buscarPorId(idUsuario);
        return reservaRepository.findByUsuarioId(idUsuario);
    }

    // -------------------------------------------------------
    // Listar multas do usuário
    // -------------------------------------------------------
    public List<Multa> listarMultas(Long idUsuario) {
        buscarPorId(idUsuario);
        return multaRepository.findByEmprestimoUsuarioId(idUsuario);
    }
}
