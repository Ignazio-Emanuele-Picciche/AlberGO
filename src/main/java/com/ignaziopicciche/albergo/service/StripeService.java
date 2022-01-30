package com.ignaziopicciche.albergo.service;

import com.ignaziopicciche.albergo.model.CardData;
import com.stripe.exception.StripeException;

public interface StripeService {
    void addPaymentMethod(CardData cardData) throws Exception;

    CardData getPaymentMethod(Long idCliente) throws StripeException;

    void detachPaymentMethod(Long idCliente) throws StripeException;
}
