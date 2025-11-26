package com.biblioteca.service;

import com.biblioteca.exception.BusinessException;
import com.biblioteca.exception.ConflictException;
import com.biblioteca.exception.NotFoundException;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Reserva;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.enums.StatusExemplar;
import com.biblioteca.model.enums.StatusReserva;
import com.biblioteca.repository.ExemplarRepository;
import com.biblioteca.repository.ReservaRepository;
import com.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExemplarRepository exemplarRepository;

    // Quantas horas uma reserva dura?
    private static final int HORAS_VALIDADE = 24;

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reserva não encontrada"));
    }

    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    // Criar reserva
    public Reserva reservar(Long idUsuario, Long idExemplar) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        Exemplar exemplar = exemplarRepository.findById(idExemplar)
                .orElseThrow(() -> new NotFoundException("Exemplar não encontrado"));

        if (exemplar.getStatus() != StatusExemplar.DISPONIVEL) {
            throw new ConflictException("Exemplar indisponível para reserva");
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setExemplar(exemplar);
        reserva.setDataReserva(LocalDateTime.now());
        reserva.setDataValidade(LocalDateTime.now().plusHours(HORAS_VALIDADE));
        reserva.setStatus(StatusReserva.ATIVA);

        return reservaRepository.save(reserva);
    }

    // Cancelar reserva
    public Reserva cancelar(Long idReserva) {

        Reserva reserva = buscarPorId(idReserva);

        if (reserva.getStatus() == StatusReserva.CANCELADA) {
            throw new BusinessException("Reserva já foi cancelada");
        }

        reserva.setStatus(StatusReserva.CANCELADA);

        return reservaRepository.save(reserva);
    }

    // Expirar reservas fora do prazo
    public void expirarReservas() {
        List<Reserva> reservas = reservaRepository.findAll();

        reservas.stream()
                .filter(r -> r.getStatus() == StatusReserva.ATIVA)
                .filter(r -> r.getDataValidade().isBefore(LocalDateTime.now()))
                .forEach(r -> {
                    r.setStatus(StatusReserva.EXPIRADA);
                    reservaRepository.save(r);
                });
    }
}
