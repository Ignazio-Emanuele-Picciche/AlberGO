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

    private Long cliente_id;
    private Long hotel_id;
}
