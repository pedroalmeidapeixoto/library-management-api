package com.biblioteca.controller;

import com.biblioteca.dto.reserva.ReservaResponseDTO;
import com.biblioteca.mapper.ReservaMapper;
import com.biblioteca.model.Reserva;
import com.biblioteca.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final ReservaMapper reservaMapper;

    public ReservaController(ReservaService reservaService, ReservaMapper reservaMapper) {
        this.reservaService = reservaService;
        this.reservaMapper = reservaMapper;
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> reservar(
            @RequestParam Long idUsuario,
            @RequestParam Long idExemplar
    ) {
        Reserva reserva = reservaService.criarReserva(idUsuario, idExemplar);
        return ResponseEntity.status(201).body(reservaMapper.toResponse(reserva));
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listar() {
        return ResponseEntity.ok(
                reservaService.listar()
                        .stream()
                        .map(reservaMapper::toResponse)
                        .toList()
        );
    }
}
