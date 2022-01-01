package com.ignaziopicciche.albergo.service.impl;

import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.dto.AmministratoreDTO;
import com.ignaziopicciche.albergo.helper.AmministratoreHelper;
import com.ignaziopicciche.albergo.service.AmministratoreService;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
    Nella classe AmministratoreService sono presenti i metodi che controllano che i dati passati dalla classe
    AmministratoreController non siano nulli, in generale controllare che i dati obbligatori non siano nulli o vuoti.
    Nel caso in cui non fossero nulli, i dati dal livello "service" verranno passati al livello "helper" che si occuperà
    dell'implementazione della logica, ovvero le operazioni, del metodo.
    Nel caso in cui, invece, qualche dato obbligatorio non fosse stato compilato, viene restituita un'eccezione nei log
    del back-end.
    Per il controllo dei campi viene usato il metodo checkArgument() della classe Preconditions (fornito dalla dependency
    Guava Preconditions), ponendo il campo obbligatorio diverso da null.

    In generale:
    Preconditions.checkArgument(!Objects.isNull("campo obbligatorio"));
 */

@Service
public class AmministratoreServiceImpl implements AmministratoreService {

    private final AmministratoreHelper amministratoreHelper;

    /**
     * In questo metodo viene implementata la logica dell'annotazione @Autowired per l'attributo amministratoreHelper,
     * ovvero stiamo chiedendo a Spring d'invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe AmministratoreHelper.
     * @param amministratoreHelper
     */
    public AmministratoreServiceImpl(AmministratoreHelper amministratoreHelper) {
        this.amministratoreHelper = amministratoreHelper;
    }

    @Override
    public Long create(AmministratoreDTO amministratoreDTO) {
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.cognome));
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.username));
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.password));
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.idHotel));

        return amministratoreHelper.create(amministratoreDTO);
    }

    @Override
    public AmministratoreDTO findAmministratoreByUsername(String username){
        Preconditions.checkArgument(!Objects.isNull(username));
        return new AmministratoreDTO(amministratoreHelper.findAmministratoreByUsername(username));
    }


}
