package com.ignaziopicciche.albergo.service.impl;

import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.helper.StripeHelper;
import com.ignaziopicciche.albergo.model.CardData;
import com.ignaziopicciche.albergo.service.StripeService;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Nella classe StripeService sono presenti i metodi che controllano che i dati passati dalla classe
 * StripeController non siano nulli, in generale controllare che i dati obbligatori non siano nulli o vuoti.
 * Nel caso in cui non fossero nulli, i dati dal livello "service" verranno passati al livello "helper" che si occuperà
 * dell'implementazione della logica, ovvero le operazioni, del metodo.
 * Nel caso in cui, invece, qualche dato obbligatorio non fosse stato compilato, viene restituita un'eccezione nei log
 * del back-end.
 * Per il controllo dei campi viene usato il metodo checkArgument() della classe Preconditions (fornito dalla dependency
 * Guava Preconditions), ponendo il campo obbligatorio diverso da null.
 * <p>
 * In generale:
 * Preconditions.checkArgument(!Objects.isNull("campo obbligatorio"));
 */

@Service
public class StripeServiceImpl implements StripeService {

    private final StripeHelper stripeHelper;

    /**
     * In questo metodo viene implementata la logica dell'annotazione @Autowired per l'attributo stripeHelper,
     * ovvero stiamo chiedendo a Spring d'invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe StripeHelper.
     *
     * @param stripeHelper
     */
    public StripeServiceImpl(StripeHelper stripeHelper) {
        this.stripeHelper = stripeHelper;
    }

    @Override
    public void addPaymentMethod(CardData cardData) throws Exception {
        Preconditions.checkArgument(!Objects.isNull(cardData.getNumber()));
        Preconditions.checkArgument(!Objects.isNull(cardData.getCvc()));
        Preconditions.checkArgument(!Objects.isNull(cardData.getExp_year()));
        Preconditions.checkArgument(!Objects.isNull(cardData.getExp_month()));
        Preconditions.checkArgument(!Objects.isNull(cardData.getIdCliente()));

        stripeHelper.addPaymentMethod(cardData);
    }

    @Override
    public CardData getPaymentMethod(Long idCliente) throws StripeException {
        Preconditions.checkArgument(!Objects.isNull(idCliente));

        return stripeHelper.getPaymentMethod(idCliente);
    }

    @Override
    public void detachPaymentMethod(Long idCliente) throws StripeException {
        Preconditions.checkArgument(!Objects.isNull(idCliente));

        stripeHelper.detachPaymentMethod(idCliente);
    }


}
