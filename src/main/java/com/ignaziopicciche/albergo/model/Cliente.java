package com.ignaziopicciche.albergo.model;

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

    @EmbeddedId
    private ClienteCustomerPK embeddedId;

    private String nome;
    private String cognome;
    private String telefono;
    private String documento;
    private String username;
    private String password;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prenotazione> prenotazioni;


    @OneToMany(mappedBy = "id.cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<ClienteHotel> clienteHotels = new HashSet<>();


}
