package com.ignaziopicciche.albergo.service;

import com.google.common.base.Preconditions;
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

    @Autowired
    private StanzaHelper stanzaHelper;

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



    public List<StanzaDTO> findStanzasOccupateByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine){
        Preconditions.checkArgument(!Objects.isNull(idHotel));
        Preconditions.checkArgument(!Objects.isNull(dataInizio));
        Preconditions.checkArgument(!Objects.isNull(dataFine));

        return stanzaHelper.findStanzasOccupateByHotel_IdAndDates(idHotel, dataInizio, dataFine);
    }

    public List<StanzaDTO> findStanzasFuoriServizioByHotel_IDAndDates(Long idHotel, Date dataInizio, Date dataFine){
        Preconditions.checkArgument(!Objects.isNull(idHotel));
        Preconditions.checkArgument(!Objects.isNull(dataInizio));
        Preconditions.checkArgument(!Objects.isNull(dataFine));

        return stanzaHelper.findStanzasFuoriServizioByHotel_IDAndDates(idHotel, dataInizio, dataFine);
    }

    public List<StanzaDTO> findStanzasByCategoria_IdAndHotel_IdAndDates(Long idCategoria, Long idHotel, Date dataInizio, Date dataFine){
        Preconditions.checkArgument(!Objects.isNull(idCategoria));
        Preconditions.checkArgument(!Objects.isNull(idHotel));
        Preconditions.checkArgument(!Objects.isNull(dataInizio));
        Preconditions.checkArgument(!Objects.isNull(dataFine));

        return stanzaHelper.findStanzasByCategoria_IdAndHotel_IdAndDates(idCategoria, idHotel, dataInizio, dataFine);
    }

    public List<StanzaDTO> findStanzasLibereByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine){
        Preconditions.checkArgument(!Objects.isNull(idHotel));
        Preconditions.checkArgument(!Objects.isNull(dataInizio));
        Preconditions.checkArgument(!Objects.isNull(dataFine));

        return stanzaHelper.findStanzasLibereByHotel_IdAndDates(idHotel, dataInizio, dataFine);
    }

    public List<StanzaDTO> FindStanzasByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine){
        Preconditions.checkArgument(!Objects.isNull(idHotel));
        Preconditions.checkArgument(!Objects.isNull(dataInizio));
        Preconditions.checkArgument(!Objects.isNull(dataFine));

        return stanzaHelper.FindStanzasByHotel_IdAndDates(idHotel, dataInizio, dataFine);
    }

}
