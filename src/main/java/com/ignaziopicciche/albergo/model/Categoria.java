package com.ignaziopicciche.albergo.model;


import com.ignaziopicciche.albergo.enums.CategoriaNome;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private CategoriaNome nome;  //Singola, Doppia, Tripla, Quadrupla
    private Double prezzo;
    private String descrizione;


    @ManyToOne
    @JoinColumn(name = "ID_CATEGORIA_HOTEL")
    private Hotel hotel;


    public Categoria(CategoriaNome nome, Double prezzo, String descrizione) {
        this.nome = nome;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
    }
}
