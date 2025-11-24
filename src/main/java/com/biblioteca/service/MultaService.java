package com.biblioteca.service;

import com.biblioteca.exception.BusinessException;
import com.biblioteca.exception.NotFoundException;
import com.biblioteca.model.Multa;
import com.biblioteca.repository.MultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MultaService {

    private final MultaRepository multaRepository;

    // -------------------------------------------------------
    // Buscar multa por ID
    // -------------------------------------------------------
    private Multa buscarPorId(Long id) {
        return multaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Multa não encontrada"));
    }

    // -------------------------------------------------------
    // Marcar multa como paga
    // -------------------------------------------------------
    public Multa marcarComoPaga(Long idMulta) {

        Multa multa = buscarPorId(idMulta);

        if (multa.isPaga()) {
            throw new BusinessException("Esta multa já está paga");
        }

        multa.setPaga(true);
        return multaRepository.save(multa);
    }

    // -------------------------------------------------------
    // Listar multas pendentes
    // -------------------------------------------------------
    public List<Multa> listarMultasPendentes() {
        return multaRepository.findByPagaFalse();
    }

    // -------------------------------------------------------
    // Listar todas as multas
    // -------------------------------------------------------
    public List<Multa> listarTodas() {
        return multaRepository.findAll();
    }
}
