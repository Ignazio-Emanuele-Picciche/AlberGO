package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.model.CardData;
import com.ignaziopicciche.albergo.service.StripeService;
import com.stripe.exception.StripeException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    private final StripeService stripeService;

    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    //Quando creo un nuovo cliente lo aggiungo negli hotel (anche se non ha la carta)
    //Quando creo una carta la aggiungo/sostituisco in tutti gli hotel
    @PostMapping("/addCard")
    public void addPaymentMethod(@RequestBody CardData cardData) throws Exception {
        stripeService.addPaymentMethod(cardData);
    }

    @GetMapping("/dettaglioCard")
    public CardData getPaymentMethod(@RequestParam("idCliente") Long idCliente) throws StripeException {
        return stripeService.getPaymentMethod(idCliente);
    }

    //Quando faccio delete card la devo eliminare in tutti hotel
    @DeleteMapping("/deleteCard")
    public void detachPaymentMethod(@RequestParam("idCliente") Long idCliente) throws StripeException {
        stripeService.detachPaymentMethod(idCliente);
    }
}
