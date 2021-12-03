package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.model.CardData;
import com.ignaziopicciche.albergo.model.PaymentMethodData;
import com.ignaziopicciche.albergo.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    //IMPORTANTE
    //@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    //@PreAuthorize("hasRole('ROLE_CLIENT')")

    //Quando creo un nuovo cliente lo aggiungo negli hotel (anche se non ha la carta)
    //Quando creo una carta la aggiungo/sostituisco in tutti gli hotel
    @PostMapping("/addCard")
    public void addPaymentMethod(@RequestBody CardData cardData) throws Exception {
        stripeService.addPaymentMethod(cardData);
    }

    @GetMapping("/dettaglioCard")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public CardData getPaymentMethod(@RequestParam("idCliente") Long idCliente) throws StripeException {
        return stripeService.getPaymentMethod(idCliente);
    }

    //Quando faccio delete card la devo eliminare in tutti hotel
    @DeleteMapping("/deleteCard")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public void detachPaymentMethod(@RequestParam("idCliente") Long idCliente) throws StripeException {
        stripeService.detachPaymentMethod(idCliente);
    }
}
