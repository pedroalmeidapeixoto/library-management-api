package com.biblioteca.repository;

import com.biblioteca.model.Usuario;
import com.biblioteca.model.enums.StatusUsuario;
import com.biblioteca.model.enums.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    List<Usuario> findByTipo(TipoUsuario tipo);

    List<Usuario> findByStatus(StatusUsuario status);

    boolean existsByEmail(String email);
}