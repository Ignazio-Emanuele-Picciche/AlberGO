package com.ignaziopicciche.albergo.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stanza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numeroStanza;
    private Boolean fuoriServizio;
    private String descrizione;
    private Integer metriQuadri;

    @OneToMany(mappedBy = "stanza", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prenotazione> prenotazioni;

    @ManyToOne
    @JoinColumn(name = "ID_STANZA_HOTEL")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "ID_STANZA_CATEGORIA")
    private Categoria categoria;

}
