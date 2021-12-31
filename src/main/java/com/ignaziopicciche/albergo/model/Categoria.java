package com.ignaziopicciche.albergo.model;


import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
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

    private Integer giorniPenale;
    private Double qtaPenale;
    private Integer giorniBlocco;


    @ManyToOne
    @JoinColumn(name = "ID_CATEGORIA_HOTEL")
    private Hotel hotel;


    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Stanza> stanze = new ArrayList<>();


}
