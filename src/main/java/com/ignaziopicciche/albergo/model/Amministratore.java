package com.ignaziopicciche.albergo.model;

import com.ignaziopicciche.albergo.model.Hotel;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
    private String ruolo;

    @ManyToOne
    @JoinColumn(name = "ID_AMMINISTRATORE_HOTEL")
    private Hotel hotel;

}
