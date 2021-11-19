package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.model.*;
import com.ignaziopicciche.albergo.repository.ClienteHotelRepository;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StripeHelper {

    private final ClienteRepository clienteRepository;
    private final HotelRepository hotelRepository;
    private final ClienteHotelRepository clienteHotelRepository;

    public StripeHelper(ClienteRepository clienteRepository, HotelRepository hotelRepository, ClienteHotelRepository clienteHotelRepository) {
        this.clienteRepository = clienteRepository;
        this.hotelRepository = hotelRepository;
        this.clienteHotelRepository = clienteHotelRepository;
    }

    //TODO implementare eccezioni per stripeHelper



    /**
     * Il metodo crea un customer stripe
     *
     * @param cliente
     * @return Cliente
     * @throws StripeException
     */
    public String createCustomer(Cliente cliente, String key) throws StripeException {
        Stripe.apiKey = key;

        Map<String, Object> params = new HashMap<>();
        params.put("name", cliente.getNome());
        params.put("phone", cliente.getTelefono());
        params.put("email", cliente.getNome()+"."+cliente.getCognome()+"@gmail.com");
        params.put("description", "Documento: " + cliente.getDocumento());

        Customer stripeCustomer = Customer.create(params);

        return stripeCustomer.getId();
    }


    public void deleteCustomerById(List<ClienteHotel> clientiHotel) throws StripeException {
        for(ClienteHotel ch: clientiHotel){
            Stripe.apiKey = ch.getHotel().getPublicKey();

            Customer customer = Customer.retrieve(ch.getCustomerId());
            customer.delete();
        }
    }

    /**
     * Il metodo aggiunge un nuovo metodo (carta) di pagamento
     *
     * @param cardData
     * @throws Exception
     */
    public PaymentMethod addPaymentMethod(CardData cardData) throws Exception {
        Hotel hotel = hotelRepository.findById(cardData.getIdHotel()).get();
        Stripe.apiKey = hotel.getPublicKey();

        Map<String, Object> card = new HashMap<>();
        card.put("number", cardData.getNumber());
        card.put("exp_month", cardData.getExp_month());
        card.put("exp_year", cardData.getExp_year());
        card.put("cvc", cardData.getCvc());
        //card.put("name", cliente.getNome()+" "+cliente.getCognome());
        Map<String, Object> paramsPaymentMehod = new HashMap<>();
        paramsPaymentMehod.put("type", "card");
        paramsPaymentMehod.put("card", card);

        PaymentMethod paymentMethod =
                PaymentMethod.create(paramsPaymentMehod);

        ClienteHotel clienteHotel = clienteHotelRepository.findByCliente_IdAndHotel_Id(cardData.getIdCliente(), cardData.getIdHotel());

        Map<String, Object> params = new HashMap<>();
        params.put("customer", clienteHotel.getCustomerId());

        PaymentMethod updatedPaymentMethod = paymentMethod.attach(params);

        //Assegno il paymentId al cliente
        clienteHotel.setPaymentMethodId(updatedPaymentMethod.getId());
        clienteHotelRepository.save(clienteHotel);

        return updatedPaymentMethod;
    }


    //TODO Ritornare la CardData
    public List<PaymentMethodData> getPaymentMethod(Long idCliente, Long idHotel) throws StripeException {
        ClienteHotel clienteHotel = clienteHotelRepository.findByCliente_IdAndHotel_Id(idCliente, idHotel);
        Hotel hotel = hotelRepository.findById(idHotel).get();
        Stripe.apiKey = hotel.getPublicKey();

        Map<String, Object> params = new HashMap<>();
        params.put("customer", clienteHotel.getCustomerId());
        params.put("type", "card");

        PaymentMethodCollection paymentMethods =
                PaymentMethod.list(params);

        List<PaymentMethodData> paymentDataList = new ArrayList<>();
        for (PaymentMethod paymentMethod : paymentMethods.getData()) {
            paymentDataList.add(PaymentMethodData.builder().paymentMethodId(paymentMethod.getId()).last4(paymentMethod.getCard().getLast4()).build());
        }

        return paymentDataList;
    }

    public void detachPaymentMethod(Long idCliente, Long idHotel) throws StripeException {
        ClienteHotel clienteHotel = clienteHotelRepository.findByCliente_IdAndHotel_Id(idCliente, idHotel);
        Hotel hotel = hotelRepository.findById(idHotel).get();
        Stripe.apiKey = hotel.getPublicKey();

        PaymentMethod paymentMethod =
                PaymentMethod.retrieve(clienteHotel.getPaymentMethodId());

        PaymentMethod updatedPaymentMethod = paymentMethod.detach();

        clienteHotel.setPaymentMethodId(null);
        clienteHotelRepository.save(clienteHotel);
    }


    /**
     * Il metodo effettua il pagamento
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

        PaymentIntent paymentIntent = PaymentIntent.create(params);
    }

}
