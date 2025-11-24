package com.biblioteca.service;

import com.biblioteca.exception.BusinessException;
import com.biblioteca.exception.ConflictException;
import com.biblioteca.exception.NotFoundException;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Reserva;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.enums.StatusExemplar;
import com.biblioteca.repository.ExemplarRepository;
import com.biblioteca.repository.ReservaRepository;
import com.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExemplarRepository exemplarRepository;

    private static final int PRAZO_RESERVA_HORAS = 48;

    // -------------------------------------------------------
    // Criar reserva
    // -------------------------------------------------------
    public Reserva criarReserva(Long idUsuario, Long idExemplar) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        Exemplar exemplar = exemplarRepository.findById(idExemplar)
                .orElseThrow(() -> new NotFoundException("Exemplar não encontrado"));

        if (exemplar.getStatus() != StatusExemplar.DISPONIVEL) {
            throw new ConflictException("Exemplar não está disponível para reserva");
        }

        // Verificar reserva duplicada ativa
        reservaRepository.findByUsuarioIdAndExemplarIdAndAtivaTrue(idUsuario, idExemplar)
                .ifPresent(r -> {
                    throw new ConflictException("Este usuário já possui uma reserva ativa para este exemplar");
                });

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setExemplar(exemplar);
        reserva.setDataReserva(LocalDate.now());
        reserva.setDataLimite(LocalDate.now().plusHours(PRAZO_RESERVA_HORAS));
        reserva.setAtiva(true);

        return reservaRepository.save(reserva);
    }

    // -------------------------------------------------------
    // Cancelar reserva
    // -------------------------------------------------------
    public Reserva cancelarReserva(Long idReserva) {

        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada"));

        if (!reserva.isAtiva()) {
            throw new BusinessException("A reserva já está finalizada ou expirada");
        }

        reserva.setAtiva(false);
        return reservaRepository.save(reserva);
    }

    // -------------------------------------------------------
    // Processar reservas expiradas
    // -------------------------------------------------------
    public void processarReservasExpiradas() {
        LocalDate agora = LocalDate.now();

        reservaRepository.findAll().forEach(reserva -> {
            if (reserva.isAtiva() && reserva.getDataLimite().isBefore(agora)) {
                reserva.setAtiva(false);
                reservaRepository.save(reserva);
            }
        });
    }
}
