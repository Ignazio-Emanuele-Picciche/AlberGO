package com.ignaziopicciche.albergo.service;

import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.helper.StripeHelper;
import com.ignaziopicciche.albergo.model.CardData;
import com.ignaziopicciche.albergo.model.PaymentMethodData;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Service
public class StripeService {

    private final StripeHelper stripeHelper;

    public StripeService(StripeHelper stripeHelper) {
        this.stripeHelper = stripeHelper;
    }

    public void addPaymentMethod(CardData cardData) throws Exception {
        Preconditions.checkArgument(!Objects.isNull(cardData.getNumber()));
        Preconditions.checkArgument(!Objects.isNull(cardData.getCvc()));
        Preconditions.checkArgument(!Objects.isNull(cardData.getExp_year()));
        Preconditions.checkArgument(!Objects.isNull(cardData.getExp_month()));
        Preconditions.checkArgument(!Objects.isNull(cardData.getIdCliente()));

        stripeHelper.addPaymentMethod(cardData);
    }

    public CardData getPaymentMethod(Long idCliente) throws StripeException{
        Preconditions.checkArgument(!Objects.isNull(idCliente));

        return stripeHelper.getPaymentMethod(idCliente);
    }

    public void detachPaymentMethod(Long idCliente) throws StripeException {
        Preconditions.checkArgument(!Objects.isNull(idCliente));

        stripeHelper.detachPaymentMethod(idCliente);
    }



}
