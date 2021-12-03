package com.ignaziopicciche.albergo.security.models;

import com.ignaziopicciche.albergo.model.Hotel;
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
    //private Ruolo ruolo = Ruolo.ROLE_ADMIN;
    private String username;
    private String password;

    @ManyToOne
    @JoinColumn(name = "ID_AMMINISTRATORE_HOTEL")
    private Hotel hotel;

}
