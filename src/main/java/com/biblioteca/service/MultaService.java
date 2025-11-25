package com.biblioteca.service;

import com.biblioteca.model.Multa;
import com.biblioteca.repository.MultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MultaService {

    private final MultaRepository multaRepository;

    public List<Multa> listar() {
        return multaRepository.findAll();
    }

    public Multa buscarPorId(Long id) {
        return multaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Multa não encontrada"));
    }
}
