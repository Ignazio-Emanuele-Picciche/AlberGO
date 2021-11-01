package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.model.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StripeHelper {


    //TODO implementare eccezioni per stripeHelper

    /**
     * Il metodo crea un account stripe
     * @param hotel
     * @return
     * @throws StripeException
     */
    public Hotel createAccount(Hotel hotel) throws StripeException {
        //Stripe.apiKey = stripeKey; TODO impl stripe apiKey

        Map<String, Object> cardPayments = new HashMap<>();
        cardPayments.put("requested", true);

        Map<String, Object> transfers = new HashMap<>();
        transfers.put("requested", true);

        Map<String, Object> capabilities = new HashMap<>();
        capabilities.put("card_payments", cardPayments);
        capabilities.put("transfers", transfers);

        Map<String, Object> business_profile = new HashMap<>();
        business_profile.put("name", hotel.getNome());

        Map<String, Object> params = new HashMap<>();
        params.put("type", "custom");
        params.put("country", "IT");
        params.put("capabilities", capabilities);
        params.put("business_profile", business_profile);

        Account account = Account.create(params);

        hotel.setAccountId(account.getId());
        //hotel.setPublicKey(account.getId());
        return hotel;
    }





    /**
     * Il metodo crea un customer stripe
     * @param cliente
     * @return Cliente
     * @throws StripeException
     */
    public Cliente createCustomer(Cliente cliente, String stripeApiKey) throws StripeException {
        //Stripe.apiKey = stripeApiKey; TODO impl stripe apiKey

        Map<String, Object> params = new HashMap<>();
        params.put("name", cliente.getNome());
        params.put("phone", cliente.getTelefono());
        params.put("description", "Documento: " + cliente.getDocumento());

        Customer stripeCustomer = Customer.create(params);

        cliente.setCustomerId(stripeCustomer.getId());
        return cliente;
    }

    /**
     * Il metodo elimina il customer stripe
     * @param stripeCustomerId
     * @throws StripeException
     */
    public void deleteCustomerById(String stripeCustomerId) throws StripeException {
        //Stripe.apiKey = stripeKey; TODO impl stripe apiKey

        Customer customer = Customer.retrieve(stripeCustomerId);
        customer.delete();
    }


    /**
     * Il metodo aggiunge un nuvo metodo (carta) di pagamento
     * @param cardData
     * @throws Exception
     */
    public void addPaymentMethod(CardData cardData /*String newPaymentMethod,*/) throws Exception {
        //Stripe.apiKey = stripeKey; TODO impl stripe apiKey

        Map<String, Object> card = new HashMap<>();
        card.put("number", cardData.getNumber());
        card.put("exp_month", cardData.getExp_month());
        card.put("exp_year", cardData.getExp_year());
        card.put("cvc", cardData.getCvc());
        Map<String, Object> paramsPaymentMehod = new HashMap<>();
        paramsPaymentMehod.put("type", "card");
        paramsPaymentMehod.put("card", card);

        PaymentMethod paymentMethod =
                PaymentMethod.create(paramsPaymentMehod);

        /*PaymentMethod paymentMethod =
                PaymentMethod.retrieve(newPaymentMethod); //idPaymentMethod*/

        Map<String, Object> params = new HashMap<>();
        params.put("customer", cardData.getCustomerId());

        PaymentMethod updatedPaymentMethod =
                paymentMethod.attach(params);
        //return updatedPaymentMethod;
    }

    /**
     * Il metodo resituisce la lista dei metodi di pagamento del customer stripe
     * @param customerId
     * @return List<PaymentMethodData>
     * @throws StripeException
     */
    public List<PaymentMethodData> getPaymentMethod(String customerId) throws StripeException {
        //Stripe.apiKey = stripeKey; TODO impl stripe apiKey

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


    /**
     * Il metodo effettua il pagamento
     * @param paymentData
     * @throws StripeException
     */
    public void createPaymentIntent(PaymentData paymentData) throws StripeException {
        //Stripe.apiKey = stripeKey; TODO impl stripe apiKey

        List<Object> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentData.getPrice());
        params.put("payment_method", paymentData.getPaymentMethod());
        params.put("currency", "eur");
        params.put("customer", paymentData.getCustomerId());
        params.put("confirm", true);
        params.put("payment_method_types", paymentMethodTypes);

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        //return paymentIntent;
    }

}
