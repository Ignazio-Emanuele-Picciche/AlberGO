package com.ignaziopicciche.albergo.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

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
    private String username;
    private String password;

    //TODO aggiunto nel db
    private String customerId;
    private String paymentMethodId;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prenotazione> prenotazioni;

    /*@ManyToOne
    @JoinColumn(name = "ID_CLIENTE_HOTEL")
    private Hotel hotel;*/

}
