package com.biblioteca.service;

import com.biblioteca.exception.BusinessException;
import com.biblioteca.exception.ConflictException;
import com.biblioteca.exception.NotFoundException;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Multa;
import com.biblioteca.model.Reserva;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.enums.StatusExemplar;
import com.biblioteca.model.enums.StatusReserva;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.ExemplarRepository;
import com.biblioteca.repository.MultaRepository;
import com.biblioteca.repository.ReservaRepository;
import com.biblioteca.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.sql.CallableStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExemplarRepository exemplarRepository;
    private final MultaRepository multaRepository;
    private final ReservaRepository reservaRepository;
    private final JdbcTemplate jdbcTemplate;

    private static final int PRAZO_PADRAO_DIAS = 7;

    // ============================================================
    // Buscar e Listar
    // ============================================================

    public Emprestimo buscarPorId(Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado"));
    }

    public List<Emprestimo> listar() {
        return emprestimoRepository.findAll();
    }

    // ============================================================
    // Criar Empréstimo
    // ============================================================

    public Emprestimo realizarEmprestimo(Long idUsuario, Long idExemplar) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        Exemplar exemplar = exemplarRepository.findById(idExemplar)
                .orElseThrow(() -> new NotFoundException("Exemplar não encontrado"));

        if (!exemplar.getStatus().equals(StatusExemplar.DISPONIVEL)) {
            throw new ConflictException("Exemplar não está disponível para empréstimo");
        }

        // Cancela reservas do usuário para esse exemplar
        reservaRepository.findByUsuarioId(idUsuario).stream()
                .filter(r -> r.getExemplar().getId().equals(idExemplar))
                .forEach(r -> {
                    r.setStatus(StatusReserva.CANCELADA);
                    reservaRepository.save(r);
                });

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setExemplar(exemplar);
        emprestimo.setDataEmprestimo(LocalDateTime.now());
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(PRAZO_PADRAO_DIAS));

        exemplar.setStatus(StatusExemplar.EMPRESTADO);
        exemplarRepository.save(exemplar);

        return emprestimoRepository.save(emprestimo);
    }

    // ============================================================
    // Devolução NORMAL
    // ============================================================

    public Emprestimo realizarDevolucao(Long idEmprestimo) {

        Emprestimo emprestimo = buscarPorId(idEmprestimo);

        if (emprestimo.getDataDevolucaoReal() != null) {
            throw new BusinessException("Este empréstimo já foi finalizado");
        }

        emprestimo.setDataDevolucaoReal(LocalDate.now());

        Exemplar exemplar = emprestimo.getExemplar();
        exemplar.setStatus(StatusExemplar.DISPONIVEL);
        exemplarRepository.save(exemplar);

        if (emprestimo.getDataDevolucaoReal().isAfter(emprestimo.getDataDevolucaoPrevista())) {
            gerarMulta(emprestimo);
        }

        return emprestimoRepository.save(emprestimo);
    }

    private void gerarMulta(Emprestimo emprestimo) {

        long diasAtraso = ChronoUnit.DAYS.between(
                emprestimo.getDataDevolucaoPrevista(),
                emprestimo.getDataDevolucaoReal()
        );

        Multa multa = new Multa();
        multa.setEmprestimo(emprestimo);
        multa.setDataAplicacao(LocalDateTime.now());
        multa.setValor(diasAtraso * 2.0);
        multa.setPago(false);

        multaRepository.save(multa);
    }

    // ============================================================
    // Devolução VIA PROCEDURE (PostgreSQL)
    // ============================================================
    @Transactional
    public void executarDevolucaoProcedure(Long idEmprestimo) {

        try (var connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement stmt = connection.prepareCall(
                     "CALL public.prc_realizar_devolucao(?::text)"
             )) {

            stmt.setInt(1, idEmprestimo.intValue());
            stmt.execute();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao executar procedure: " + e.getMessage(), e);
        }
    }

}
