package com.ignaziopicciche.albergo.service;


import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.dto.StanzaDTO;
import com.ignaziopicciche.albergo.helper.StanzaHelper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
    Nella classe StanzaService sono presenti i metodi che controllano che i dati passati dalla classe
    StanzaController non siano nulli, in generale controllare che i dati obbligatori non siano nulli o vuoti.
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
public class StanzaService {

    private final StanzaHelper stanzaHelper;

    /**
     * In questo metodo viene implementata la logica dell'annotazione @Autowired per l'attributo stanzaHelper,
     * ovvero stiamo chiedendo a Spring d'invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe StanzaHelper.
     * @param stanzaHelper
     */
    public StanzaService(StanzaHelper stanzaHelper) {
        this.stanzaHelper = stanzaHelper;
    }

    public StanzaDTO create(StanzaDTO stanzaDTO) {
        Preconditions.checkArgument(!Objects.isNull(stanzaDTO.numeroStanza));
        Preconditions.checkArgument(!Objects.isNull(stanzaDTO.fuoriServizio));
        Preconditions.checkArgument(!Objects.isNull(stanzaDTO.descrizione));
        Preconditions.checkArgument(!Objects.isNull(stanzaDTO.metriQuadri));
        Preconditions.checkArgument(!Objects.isNull(stanzaDTO.idHotel));
        Preconditions.checkArgument(!Objects.isNull(stanzaDTO.idCategoria));

        return stanzaHelper.create(stanzaDTO);
    }

    public Boolean delete(Long id) {
        Preconditions.checkArgument(!Objects.isNull(id));

        return stanzaHelper.delete(id);
    }

    public StanzaDTO update(StanzaDTO stanzaDTO) {
        Preconditions.checkArgument(!Objects.isNull(stanzaDTO.id));
        //Preconditions.checkArgument(!Objects.isNull(stanzaDTO.numeroStanza));
        Preconditions.checkArgument(!Objects.isNull(stanzaDTO.fuoriServizio));
        Preconditions.checkArgument(!Objects.isNull(stanzaDTO.descrizione));
        //Preconditions.checkArgument(!Objects.isNull(stanzaDTO.metriQuadri));
        //Preconditions.checkArgument(!Objects.isNull(stanzaDTO.idHotel));
        //Preconditions.checkArgument(!Objects.isNull(stanzaDTO.idCategoria));

        return stanzaHelper.update(stanzaDTO);
    }


    public StanzaDTO findById(Long id){
        Preconditions.checkArgument(!Objects.isNull(id));

        return stanzaHelper.findById(id);
    }

    public List<StanzaDTO> findAll(Long idHotel){
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return stanzaHelper.findAll(idHotel);
    }



    public List<StanzaDTO> findStanzasByCategoria_IdAndDates(Long idCategoria, Date dataInizio, Date dataFine){
        Preconditions.checkArgument(!Objects.isNull(idCategoria));
        Preconditions.checkArgument(!Objects.isNull(dataInizio));
        Preconditions.checkArgument(!Objects.isNull(dataFine));

        return stanzaHelper.findStanzasByCategoria_IdAndDates(idCategoria, dataInizio, dataFine);
    }


    public int findCountStanzasFuoriServizioByHotel_Id(Long idHotel){
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return stanzaHelper.findCountStanzasFuoriServizioByHotel_Id(idHotel);
    }

    public List<StanzaDTO> findStanzasLibereByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine){
        Preconditions.checkArgument(!Objects.isNull(idHotel));
        Preconditions.checkArgument(!Objects.isNull(dataFine));
        Preconditions.checkArgument(!Objects.isNull(dataInizio));

        return stanzaHelper.findStanzasLibereByHotel_IdAndDates(idHotel, dataInizio, dataFine);
    }


    public List<StanzaDTO> findStanzasOccupateByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine){
        Preconditions.checkArgument(!Objects.isNull(idHotel));
        Preconditions.checkArgument(!Objects.isNull(dataFine));
        Preconditions.checkArgument(!Objects.isNull(dataInizio));

        return stanzaHelper.findStanzasOccupateByHotel_IdAndDates(idHotel, dataInizio, dataFine);
    }

    public int findCountStanzeByCategoria_Id(Long idCategoria){
        Preconditions.checkArgument(!Objects.isNull(idCategoria));

        return stanzaHelper.findCountStanzeByCategoria_Id(idCategoria);
    }

}
