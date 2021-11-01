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
    private String brand;
    private String country;
    private String customerId;
    private String exp_month;
    private String exp_year;
    private String fingerprint;
    private String last4;


}
