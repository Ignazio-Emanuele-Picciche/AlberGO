package com.ignaziopicciche.albergo.model;

import com.ignaziopicciche.albergo.enums.Ruolo;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cognome;
    private String telefono;
    private String documento;
    //private Ruolo ruolo = Ruolo.ROLE_CLIENT;
    private String username;
    private String password;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prenotazione> prenotazioni;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ClienteHotel> clientiHotel;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE_HOTEL")
    private Hotel hotel;

}
