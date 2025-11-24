package com.biblioteca.dto.reserva;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservaResponseDTO {
    private Long id;
    private Long idUsuario;
    private Long idExemplar;
    private LocalDate dataReserva;
    private LocalDate dataLimite;
    private boolean ativa;
}
