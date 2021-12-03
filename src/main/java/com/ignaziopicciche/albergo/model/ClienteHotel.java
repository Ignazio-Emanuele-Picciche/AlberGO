package com.ignaziopicciche.albergo.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteHotel /*implements Serializable*/ {

    //@EmbeddedId
    //private ClienteHotelPK id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    //@MapsId("cliente_id")
    @JoinColumn(name = "ID_CLIENTE")
    private Cliente cliente;

    @ManyToOne
    //@MapsId("hotel_id")
    @JoinColumn(name = "ID_HOTEL")
    private Hotel hotel;

    private String customerId;
    private String paymentMethodId;
    private String cvc;


}
