package com.ignaziopicciche.albergo.model;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Double prezzo;
    private String descrizione;

    //TODO aggiornare giovanni. Sono stati aggiunti questi campi
    //fino a n giorni prima puo modifica/cancellare la prenotazione
    private Integer giorniPenale;

    //accredito penale
    private Double qtaPenale;

    //n giorni prima, dopo i quali non si puo piu cancellare/modificare la prenotazione
    private Integer giorniBlocco;


    @ManyToOne
    @JoinColumn(name = "ID_CATEGORIA_HOTEL")
    private Hotel hotel;


    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Stanza> stanze;


}
