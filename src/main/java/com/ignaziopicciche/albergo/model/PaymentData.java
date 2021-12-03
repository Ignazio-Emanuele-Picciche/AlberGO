package com.ignaziopicciche.albergo.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentData {

    private String paymentMethod;
    private String price;
    private String customerId;
    private String key;
    private String description;

}
