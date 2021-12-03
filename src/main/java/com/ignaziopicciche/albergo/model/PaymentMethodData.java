package com.ignaziopicciche.albergo.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodData {
    private String last4;
    private String paymentMethodId;
}
