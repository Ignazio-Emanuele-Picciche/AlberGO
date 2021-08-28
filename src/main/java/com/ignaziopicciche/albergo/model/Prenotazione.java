package com.ignaziopicciche.albergo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataInizio;
    private LocalDate dataFine;

    @ManyToOne
    @JoinColumn(name = "ID_PRENOTAZIONE_CLIENTE")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "ID_PRENOTAZIONE_HOTEL")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "ID_PRENOTAZIONE_STANZA")
    private Stanza stanza;

}
