package com.biblioteca.model;

import com.biblioteca.model.enums.StatusReserva;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
@Data
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_exemplar", nullable = false)
    private Exemplar exemplar;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "data_reserva")
    private LocalDateTime dataReserva;

    @Column(name = "data_validade")
    private LocalDateTime dataValidade;

    @Enumerated(EnumType.STRING)
    private StatusReserva status;
}
