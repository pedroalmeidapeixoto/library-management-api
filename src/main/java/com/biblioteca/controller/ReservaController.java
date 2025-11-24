package com.biblioteca.controller;

import com.biblioteca.model.Reserva;
import com.biblioteca.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @PostMapping
    public Reserva criar(
            @RequestParam Long idUsuario,
            @RequestParam Long idExemplar
    ) {
        return reservaService.criarReserva(idUsuario, idExemplar);
    }

    @PostMapping("/{id}/cancelar")
    public Reserva cancelar(@PathVariable Long id) {
        return reservaService.cancelarReserva(id);
    }

    @PostMapping("/processar-expiradas")
    public void processarExpiradas() {
        reservaService.processarReservasExpiradas();
    }
}
