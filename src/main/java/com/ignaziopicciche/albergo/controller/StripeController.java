package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.model.CardData;
import com.ignaziopicciche.albergo.model.PaymentMethodData;
import com.ignaziopicciche.albergo.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    //TODO aggiornare le api
    //TODO chiedere a giovanni
    //I clienti quando e dove si possono creare?
    @PostMapping("/addPaymentMethod")
    public PaymentMethod addPaymentMethod(@RequestBody CardData cardData) throws Exception {
        return stripeService.addPaymentMethod(cardData);
    }


    //TODO aggiornare api
    @GetMapping("/listaPaymentMethod")
    public List<PaymentMethodData> getPaymentMethod(@RequestParam("customerId") String customerId) throws StripeException {
        return stripeService.getPaymentMethod(customerId);
    }

    @DeleteMapping("/detachPaymentMethod")
    public void detachPaymentMethod(@RequestParam("idCliente") Long idCliente) throws StripeException {
        stripeService.detachPaymentMethod(idCliente);
    }

}
