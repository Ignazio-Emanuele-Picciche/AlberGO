package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.model.CardData;
import com.ignaziopicciche.albergo.service.StripeService;
import com.stripe.exception.StripeException;
import org.springframework.web.bind.annotation.*;

/**
 * -Nella classe StripeController vengono gestiti e organizzati tutti gli endpoint relativi alle operazioni che si possono fare con la carta.
 * -I path delle api, ovvero delle attività che si possono svolgere relative alla carta, iniziano con:
 * "http://localhost:8080/api/stripe/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe StripeService
 * per il controllo e la validità dei dati in input delle request dal front-end.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    private final StripeService stripeService;

    /**
     * In questo metodo viene implementata la logica dell'annotazione @Autowired per l'attributo stripeService,
     * ovvero stiamo chiedendo a Spring d'invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe StripeService.
     *
     * @param stripeService
     */
    public StripeController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    /**
     * Endpoint che aggiunge la carta associata al cliente
     *
     * @param cardData
     * @throws Exception
     */
    //Quando creo un nuovo cliente lo aggiungo negli hotel (anche se non ha la carta)
    //Quando creo una carta la aggiungo/sostituisco in tutti gli hotel
    @PostMapping("/addCard")
    public void addPaymentMethod(@RequestBody CardData cardData) throws Exception {
        stripeService.addPaymentMethod(cardData);
    }

    /**
     * Endpoint che restituisce i dati della carta di un cliente
     *
     * @param idCliente
     * @return CardData
     * @throws StripeException
     */
    @GetMapping("/dettaglioCard")
    public CardData getPaymentMethod(@RequestParam("idCliente") Long idCliente) throws StripeException {
        return stripeService.getPaymentMethod(idCliente);
    }

    /**
     * Endpoint che elimina la carta del cliente
     *
     * @param idCliente
     * @throws StripeException
     */
    //Quando faccio delete card la devo eliminare in tutti hotel
    @DeleteMapping("/deleteCard")
    public void detachPaymentMethod(@RequestParam("idCliente") Long idCliente) throws StripeException {
        stripeService.detachPaymentMethod(idCliente);
    }
}
