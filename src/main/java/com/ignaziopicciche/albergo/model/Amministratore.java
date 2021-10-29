package com.ignaziopicciche.albergo.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Amministratore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
    private String username;
    private String password;

    @ManyToOne
    @JoinColumn(name = "ID_AMMINISTRATORE_HOTEL")
    private Hotel hotel;
}
