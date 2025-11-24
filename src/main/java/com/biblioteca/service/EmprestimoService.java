package com.biblioteca.service;

import com.biblioteca.exception.BusinessException;
import com.biblioteca.exception.ConflictException;
import com.biblioteca.exception.NotFoundException;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Multa;
import com.biblioteca.model.Reserva;
import com.biblioteca.model.Usuario;
import com.biblioteca.model.enums.StatusEmprestimo;
import com.biblioteca.model.enums.StatusExemplar;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.ExemplarRepository;
import com.biblioteca.repository.MultaRepository;
import com.biblioteca.repository.ReservaRepository;
import com.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExemplarRepository exemplarRepository;
    private final MultaRepository multaRepository;
    private final ReservaRepository reservaRepository;

    private static final int PRAZO_PADRAO_DIAS = 7;
    private static final double MULTA_POR_DIA = 2.0;

    // ------------------------------------------------------------------------
    // Buscar empréstimo
    // ------------------------------------------------------------------------
    public Emprestimo buscarPorId(Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado"));
    }

    // ------------------------------------------------------------------------
    // Realizar empréstimo
    // ------------------------------------------------------------------------
    public Emprestimo realizarEmprestimo(Long idUsuario, Long idExemplar) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        Exemplar exemplar = exemplarRepository.findById(idExemplar)
                .orElseThrow(() -> new NotFoundException("Exemplar não encontrado"));

        if (exemplar.getStatus() != StatusExemplar.DISPONIVEL) {
            throw new ConflictException("Exemplar não está disponível para empréstimo");
        }

        // Cancelar reservas anteriores do usuário para esse exemplar (opcional)
        reservaRepository.findByUsuarioIdAndExemplarIdAndAtivaTrue(idUsuario, idExemplar)
                .ifPresent(r -> {
                    r.setAtiva(false);
                    reservaRepository.save(r);
                });

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setExemplar(exemplar);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataPrevistaDevolucao(LocalDate.now().plusDays(PRAZO_PADRAO_DIAS));
        emprestimo.setStatus(StatusEmprestimo.ATIVO);

        exemplar.setStatus(StatusExemplar.EMPRESTADO);
        exemplarRepository.save(exemplar);

        return emprestimoRepository.save(emprestimo);
    }

    // ------------------------------------------------------------------------
    // Finalizar empréstimo (devolução)
    // ------------------------------------------------------------------------
    public Emprestimo finalizarEmprestimo(Long idEmprestimo) {

        Emprestimo emprestimo = emprestimoRepository.findById(idEmprestimo)
                .orElseThrow(() -> new NotFoundException("Empréstimo não encontrado"));

        if (emprestimo.getStatus() != StatusEmprestimo.ATIVO) {
            throw new BusinessException("Este empréstimo já foi finalizado");
        }

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatus(StatusEmprestimo.FINALIZADO);

        // Liberar exemplar
        Exemplar exemplar = emprestimo.getExemplar();
        exemplar.setStatus(StatusExemplar.DISPONIVEL);
        exemplarRepository.save(exemplar);

        // Gerar multa se houver atraso
        if (emprestimo.getDataDevolucao().isAfter(emprestimo.getDataPrevistaDevolucao())) {
            gerarMulta(emprestimo);
        }

        return emprestimoRepository.save(emprestimo);
    }

    // ------------------------------------------------------------------------
    // Calcular multa
    // ------------------------------------------------------------------------
    private void gerarMulta(Emprestimo emprestimo) {

        long diasAtraso = ChronoUnit.DAYS.between(
                emprestimo.getDataPrevistaDevolucao(),
                LocalDate.now()
        );

        double valor = diasAtraso * MULTA_POR_DIA;

        Multa multa = new Multa();
        multa.setEmprestimo(emprestimo);
        multa.setDataGeracao(LocalDate.now());
        multa.setValor(valor);
        multa.setPaga(false);

        multaRepository.save(multa);
    }
}
