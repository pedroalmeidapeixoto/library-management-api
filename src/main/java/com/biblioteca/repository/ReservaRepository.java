package com.biblioteca.repository;

import com.biblioteca.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioId(Long usuarioId);
    Optional<Reserva> findByUsuarioIdAndExemplarIdAndAtivaTrue(Long usuarioId, Long exemplarId);
}
