package com.biblioteca.service;

import com.biblioteca.dto.usuario.UsuarioDTO;
import com.biblioteca.dto.usuario.UsuarioResponseDTO;
import com.biblioteca.mapper.UsuarioMapper;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.enums.StatusUsuario;
import com.biblioteca.model.enums.TipoUsuario;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Transactional
    public UsuarioResponseDTO salvar(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(usuarioSalvo);
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioResponseDTO> buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toResponseDTO);
    }

    public Optional<UsuarioResponseDTO> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuarioMapper::toResponseDTO);
    }

    public List<UsuarioResponseDTO> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> buscarPorTipo(TipoUsuario tipo) {
        return usuarioRepository.findByTipo(tipo).stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> buscarPorStatus(StatusUsuario status) {
        return usuarioRepository.findByStatus(status).stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioDTO usuarioDTO) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    if (!usuario.getEmail().equals(usuarioDTO.getEmail()) &&
                            usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                        throw new RuntimeException("Email já cadastrado");
                    }

                    usuarioMapper.updateEntityFromDTO(usuarioDTO, usuario);
                    Usuario usuarioAtualizado = usuarioRepository.save(usuario);
                    return usuarioMapper.toResponseDTO(usuarioAtualizado);
                })
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
    }

    @Transactional
    public void deletar(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}