package com.ignaziopicciche.albergo.model;


import lombok.*;

import javax.persistence.*;

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

    private Integer giorniPenale;
    private Double qtaPenale;
    private Integer giorniBlocco;


    @ManyToOne
    @JoinColumn(name = "ID_CATEGORIA_HOTEL")
    private Hotel hotel;


}
