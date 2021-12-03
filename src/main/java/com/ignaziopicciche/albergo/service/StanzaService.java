package com.ignaziopicciche.albergo.service;


import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.dto.StanzaDTO;
import com.ignaziopicciche.albergo.helper.StanzaHelper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class StanzaService {

    private final StanzaHelper stanzaHelper;

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
