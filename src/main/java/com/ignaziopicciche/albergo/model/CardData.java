package com.ignaziopicciche.albergo.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardData {

    private String cardId;
    private String number;
    private String cvc;
    private Long idCliente;
    private Long idHotel;
    private String exp_month;
    private String exp_year;
}
