package com.ignaziopicciche.albergo.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Servizio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Double prezzo;


    @ManyToOne
    @JoinColumn(name = "ID_SERVIZIO_HOTEL")
    private Hotel hotel;


    @ManyToMany
    @JoinTable(name = "Servizio_Prenotazione", joinColumns =
    @JoinColumn(name = "servizioId"), inverseJoinColumns =
    @JoinColumn(name = "prenotazioneId"))
    @Singular("prenotazione")
    private List<Prenotazione> prenotazioni;




}
