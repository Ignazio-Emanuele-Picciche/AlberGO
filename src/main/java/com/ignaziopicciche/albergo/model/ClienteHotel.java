package com.ignaziopicciche.albergo.model;

import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.IdClass;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ClienteHotelId.class)
public class ClienteHotel {

    @EmbeddedId
    @Builder.Default
    private ClienteHotelId id = new ClienteHotelId();

}
