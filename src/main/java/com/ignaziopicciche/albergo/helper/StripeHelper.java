package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.exception.enums.ClienteEnum;
import com.ignaziopicciche.albergo.exception.enums.ClienteHotelEnum;
import com.ignaziopicciche.albergo.exception.enums.HotelEnum;
import com.ignaziopicciche.albergo.exception.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.*;
import com.ignaziopicciche.albergo.repository.ClienteHotelRepository;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La classe StripeHelper contiene i metodi che si occupano dell'implementazione delle logiche
 * e funzionalità vere e proprie degli endpoint richiamati dal front-end. I dati che vengono forniti a questi metodi
 * provengono dal livello "service" nel quale è stato controllato che i campi obbligatori sono stati inseriti correttamente
 * nel front-end.
 * Per "logiche e funzionalita" si intende:
 * -es. aggiungere un nuovo cliente nel sistema stripe;
 * -es. associare una carta ad un cliente stripe.
 * -varie operazioni di logica (calcoli, operazioni, controlli generici)
 * -restituire, al front-end, le eccezioni custom in caso di errore
 * -in caso di operazioni andate a buon fine, verranno restituiti al livello service i dati che dovranno essere inviati al front-end.
 */

@Component
public class StripeHelper {

    private final ClienteRepository clienteRepository;
    private final HotelRepository hotelRepository;
    private final ClienteHotelRepository clienteHotelRepository;

    private static ClienteEnum clienteEnum;
    private static ClienteHotelEnum clienteHotelEnum;
    private static HotelEnum hotelEnum;

    public StripeHelper(ClienteRepository clienteRepository, HotelRepository hotelRepository, ClienteHotelRepository clienteHotelRepository) {
        this.clienteRepository = clienteRepository;
        this.hotelRepository = hotelRepository;
        this.clienteHotelRepository = clienteHotelRepository;
    }


    /**
     * Il metodo crea un account stripe (al cliente) basandosi sui dati cliente presente nel sistema
     *
     * @param cliente
     * @return customerId
     * @throws StripeException
     */
    public String createCustomer(Cliente cliente, String key) throws Exception {
        Stripe.apiKey = key;

        Map<String, Object> params = new HashMap<>();
        params.put("name", cliente.getNome());
        params.put("phone", cliente.getTelefono());
        params.put("email", cliente.getNome() + "." + cliente.getCognome() + "@gmail.com");
        params.put("description", "Documento: " + cliente.getDocumento());

        Customer stripeCustomer = Customer.create(params);

        return stripeCustomer.getId();
    }

    /**
     * Il metodo associa la carta, cui cui fare i pagamenti, al cliente passato come parametro
     *
     * @param cliente
     * @throws Exception
     */
    public void addClienteHotelCarta(Cliente cliente) throws Exception {
        if (!clienteHotelRepository.findAll().isEmpty()) {
            List<ClienteHotel> clienti = clienteHotelRepository.findByCliente_Id(cliente.getId());
            for (ClienteHotel clienteHotel : clienti) {
                if (StringUtils.isNotBlank(clienteHotel.getPaymentMethodId())) {
                    CardData card = getPaymentMethodByClienteHotel(clienteHotel);
                    card.setIdCliente(cliente.getId());
                    //card.setIdHotel(hotelRepository.findByPublicKey(key).getId());
                    addPaymentMethod(card);
                }
            }
        }
    }

    /**
     * Il metodo restituisce i dettagli della carta associata al cliente e all'hotel
     *
     * @param clienteHotel
     * @return CardData
     * @throws StripeException
     */
    public CardData getPaymentMethodByClienteHotel(ClienteHotel clienteHotel) throws StripeException {
        Stripe.apiKey = clienteHotel.getHotel().getPublicKey();

        PaymentMethod paymentMethod = PaymentMethod.retrieve(clienteHotel.getPaymentMethodId());

        return CardData.builder()
                .number("4242424242424242")
                .cvc(clienteHotel.getCvc())
                .exp_month(paymentMethod.getCard().getExpMonth().toString())
                .exp_year(paymentMethod.getCard().getExpYear().toString()).build();
    }

    /**
     * Il metodo elimina l'account stripe del cliente asscoaito all'hotel
     * @param clientiHotel
     * @throws StripeException
     */
    /*public void deleteCustomerById(List<ClienteHotel> clientiHotel) throws StripeException {
        for (ClienteHotel ch : clientiHotel) {
            Stripe.apiKey = ch.getHotel().getPublicKey();

            Customer customer = Customer.retrieve(ch.getCustomerId());
            customer.delete();
        }
    }*/

    /**
     * Il metodo aggiunge una nuovo metodo di pagamento, ovvero aggiunge una nuova carta nel sistema stripe e l'associa al cliente proprietario
     *
     * @param cardData
     * @throws Exception
     */
    public void addPaymentMethod(CardData cardData) throws Exception {

        if (hotelRepository.findAll().size() <= 0) {

            if (clienteRepository.existsById(cardData.getIdCliente())) {
                List<Hotel> hotels = hotelRepository.findAll();

                if (!hotels.isEmpty()) {
                    hotels.forEach(hotel -> {

                        Stripe.apiKey = hotel.getPublicKey();

                        Map<String, Object> card = new HashMap<>();
                        card.put("number", cardData.getNumber());
                        card.put("exp_month", cardData.getExp_month());
                        card.put("exp_year", cardData.getExp_year());
                        card.put("cvc", cardData.getCvc());
                        Map<String, Object> paramsPaymentMehod = new HashMap<>();
                        paramsPaymentMehod.put("type", "card");
                        paramsPaymentMehod.put("card", card);

                        PaymentMethod paymentMethod = null;
                        try {
                            paymentMethod = PaymentMethod.create(paramsPaymentMehod);
                        } catch (StripeException e) {
                            clienteHotelEnum = ClienteHotelEnum.getClienteHotelEnumByMessageCode("CREATE_PM");
                            throw new ApiRequestException(clienteHotelEnum.getMessage());
                        }

                        ClienteHotel clienteHotel = clienteHotelRepository.findByCliente_IdAndHotel_Id(cardData.getIdCliente(), hotel.getId());

                        if (clienteHotel != null && StringUtils.isBlank(clienteHotel.getPaymentMethodId())) {
                            clienteHotel.setCvc(cardData.getCvc());

                            Map<String, Object> params = new HashMap<>();
                            params.put("customer", clienteHotel.getCustomerId());

                            PaymentMethod updatedPaymentMethod = null;

                            try {
                                if (paymentMethod != null) {
                                    updatedPaymentMethod = paymentMethod.attach(params);
                                } else {
                                    clienteHotelEnum = ClienteHotelEnum.getClienteHotelEnumByMessageCode("PM_NF");
                                    throw new ApiRequestException(clienteHotelEnum.getMessage());
                                }
                            } catch (StripeException e) {
                                clienteHotelEnum = ClienteHotelEnum.getClienteHotelEnumByMessageCode("ATTCARD");
                                throw new ApiRequestException(clienteHotelEnum.getMessage());
                            }

                            //Assegno il paymentId al cliente
                            if (updatedPaymentMethod != null) {
                                clienteHotel.setPaymentMethodId(updatedPaymentMethod.getId());
                                clienteHotelRepository.save(clienteHotel);
                            } else {
                                clienteHotelEnum = ClienteHotelEnum.getClienteHotelEnumByMessageCode("ATTCARD");
                                throw new ApiRequestException(clienteHotelEnum.getMessage());
                            }

                        }
                    });
                }
            } else {
                clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_IDNE");
                throw new ApiRequestException(clienteEnum.getMessage());
            }
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_EPT");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    /**
     * Il metodo restituisce i dettagli della carta associata al cliente
     *
     * @param idCliente
     * @return CardData
     * @throws StripeException
     */
    public CardData getPaymentMethod(Long idCliente) throws StripeException {
        List<ClienteHotel> clientiHotel = clienteHotelRepository.findByCliente_Id(idCliente);

        ClienteHotel clienteHotel = clientiHotel.stream().filter(clienteHotelApp -> clienteHotelApp.getPaymentMethodId() != null).findFirst().orElse(null);
        if (clienteHotel == null) {
            return new CardData();
        }

        Stripe.apiKey = clienteHotel.getHotel().getPublicKey();
        PaymentMethod paymentMethod = PaymentMethod.retrieve(clienteHotel.getPaymentMethodId());

        return CardData.builder()
                .paymentMethodId(paymentMethod.getId())
                .idCliente(idCliente)
                .number("4242424242424242")
                .cvc(clienteHotel.getCvc())
                .exp_month(paymentMethod.getCard().getExpMonth().toString())
                .exp_year(paymentMethod.getCard().getExpYear().toString()).build();
    }

    /**
     * Il metodo elimina, in tutto il sistema stripe e in tutti gli hotel, la carta associata al cliente
     *
     * @param idCliente
     * @throws StripeException
     */
    public void detachPaymentMethod(Long idCliente) throws StripeException {
        List<ClienteHotel> clientiHotel = clienteHotelRepository.findByCliente_Id(idCliente);

        clientiHotel.forEach(clienteHotel -> {
            Stripe.apiKey = clienteHotel.getHotel().getPublicKey();

            PaymentMethod paymentMethod = null;
            try {
                paymentMethod = PaymentMethod.retrieve(clienteHotel.getPaymentMethodId());
            } catch (StripeException e) {
                clienteHotelEnum = ClienteHotelEnum.getClienteHotelEnumByMessageCode("DELETE_PM");
                throw new ApiRequestException(clienteHotelEnum.getMessage());
            }

            try {
                if (paymentMethod != null) {
                    PaymentMethod updatedPaymentMethod = paymentMethod.detach();
                } else {
                    clienteHotelEnum = ClienteHotelEnum.getClienteHotelEnumByMessageCode("PM_NF");
                    throw new ApiRequestException(clienteHotelEnum.getMessage());
                }
            } catch (StripeException e) {
                clienteHotelEnum = ClienteHotelEnum.getClienteHotelEnumByMessageCode("DELETE_PM");
                throw new ApiRequestException(clienteHotelEnum.getMessage());
            }

            clienteHotel.setPaymentMethodId(null);
            clienteHotelRepository.save(clienteHotel);

        });
    }


    /**
     * Il metodo effettua il pagamento. Ad esempio l'addebito di una nuova prenotazione o l'addebito assegnato per aver inserito
     * un servizio nella prenotazione.
     *
     * @param paymentData
     * @throws StripeException
     */
    public void createPaymentIntent(PaymentData paymentData) throws StripeException {
        Stripe.apiKey = paymentData.getKey();

        List<Object> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentData.getPrice());
        params.put("payment_method", paymentData.getPaymentMethod());
        params.put("currency", "eur");
        params.put("description", paymentData.getDescription());
        params.put("customer", paymentData.getCustomerId());
        params.put("confirm", true);
        params.put("payment_method_types", paymentMethodTypes);

        PaymentIntent.create(params);
    }

}
