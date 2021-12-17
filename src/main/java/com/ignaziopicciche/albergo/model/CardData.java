package com.ignaziopicciche.albergo.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardData {

    private String paymentMethodId;
    private String number;
    private String cvc;
    private Long idCliente;
    private String exp_month;
    private String exp_year;
}
