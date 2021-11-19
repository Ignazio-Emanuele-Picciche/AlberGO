package com.ignaziopicciche.albergo.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteHotelPK implements Serializable {

    @Column(name = "ID_CLIENTE")
    private Long cliente_id;

    @Column(name = "ID_HOTEL")
    private Long hotel_id;
}
