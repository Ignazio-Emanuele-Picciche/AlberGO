package com.ignaziopicciche.albergo.security.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ignaziopicciche.albergo.model.Hotel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Amministratore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
    private String username;

    @JsonIgnore
    private String password;

    @ManyToOne
    @JoinColumn(name = "ID_AMMINISTRATORE_HOTEL")
    private Hotel hotel;

}
