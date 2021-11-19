package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.model.*;
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

    @Value("${customerBank}")
    String customerBank;

    private final ClienteRepository clienteRepository;
    private final HotelRepository hotelRepository;

    public StripeHelper(ClienteRepository clienteRepository, HotelRepository hotelRepository) {
        this.clienteRepository = clienteRepository;
        this.hotelRepository = hotelRepository;
    }

    //TODO implementare eccezioni per stripeHelper


    public void insertKey(String key, Long idHotel) throws StripeException {
        if(hotelRepository.existsById(idHotel)){
            Hotel hotel = hotelRepository.findById(idHotel).get();
            hotel.setPublicKey(key);


            List<Cliente> clienti = clienteRepository.findAll();
            for(Cliente c: clienti){
               c = createCustomer(c, key);
               //hotel.addCliente(c);
               clienteRepository.save(c);
            }

            hotelRepository.save(hotel);
        }
    }


    /**
     * Il metodo crea un customer stripe
     *
     * @param cliente
     * @return Cliente
     * @throws StripeException
     */
    public Cliente createCustomer(Cliente cliente, String key) throws StripeException {
        //Stripe.apiKey = key;

        Map<String, Object> params = new HashMap<>();
        params.put("name", cliente.getNome());
        params.put("phone", cliente.getTelefono());
        params.put("email", cliente.getNome()+"."+cliente.getCognome()+"@gmail.com");
        params.put("description", "Documento: " + cliente.getDocumento());

        Customer stripeCustomer = Customer.create(params);

        cliente.getEmbeddedId().setCustomerId(stripeCustomer.getId());
        return cliente;
    }

    /**
     * Il metodo elimina il customer stripe
     *
     * @param stripeCustomerId
     * @throws StripeException
     */
    public void deleteCustomerById(String stripeCustomerId) throws StripeException {
        Stripe.apiKey = customerBank;

        Customer customer = Customer.retrieve(stripeCustomerId);
        customer.delete();
    }


    /**
     * Il metodo aggiunge un nuovo metodo (carta) di pagamento
     *
     * @param cardData
     * @throws Exception
     */
    public PaymentMethod addPaymentMethod(CardData cardData) throws Exception {
        Stripe.apiKey = customerBank;

        Cliente cliente = clienteRepository.findById(cardData.getIdCliente()).get();

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


        Map<String, Object> params = new HashMap<>();
        params.put("customer", cliente.getEmbeddedId().getCustomerId());

        PaymentMethod updatedPaymentMethod =
                paymentMethod.attach(params);

        //Assegno il paymentId al cliente
        cliente.getEmbeddedId().setPaymentMethodId(updatedPaymentMethod.getId());
        clienteRepository.save(cliente);

        return updatedPaymentMethod;
    }


    /**
     * Il metodo resituisce la lista dei metodi di pagamento del customer stripe
     *
     * @param customerId
     * @return List<PaymentMethodData>
     * @throws StripeException
     */
    public List<PaymentMethodData> getPaymentMethod(String customerId) throws StripeException {
        Stripe.apiKey = customerBank;

        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        params.put("type", "card");

        PaymentMethodCollection paymentMethods =
                PaymentMethod.list(params);

        List<PaymentMethodData> paymentDataList = new ArrayList<>();
        for (PaymentMethod paymentMethod : paymentMethods.getData()) {
            paymentDataList.add(PaymentMethodData.builder().paymentMethodId(paymentMethod.getId()).last4(paymentMethod.getCard().getLast4()).build());
        }

        return paymentDataList;
    }

    public void detachPaymentMethod(Long idCliente) throws StripeException {
        Stripe.apiKey = customerBank;

        Cliente cliente = clienteRepository.findById(idCliente).get();
        cliente.getEmbeddedId().setPaymentMethodId(null);
        clienteRepository.save(cliente);

        PaymentMethod paymentMethod =
                PaymentMethod.retrieve(cliente.getEmbeddedId().getPaymentMethodId());

        PaymentMethod updatedPaymentMethod =
                paymentMethod.detach();
    }


    /**
     * Il metodo effettua il pagamento
     *
     * @param paymentData
     * @throws StripeException
     */
    public void createPaymentIntent(PaymentData paymentData) throws StripeException {
        Stripe.apiKey = "sk_test_51JxT04DcIwVJon7p1Sz1Ateg0DFCTF8Dn9L3rZWRcB17AM85vch7Njcfr9pu84SZJt4fILUutMVkKKk1VkF1M3TS00vf3qtGoj";

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
