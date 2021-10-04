package com.ignaziopicciche.albergo.service;

import com.google.common.base.Preconditions;
import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.helper.PrenotazioneHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PrenotazioneService {

    private final PrenotazioneHelper prenotazioneHelper;

    public PrenotazioneService(PrenotazioneHelper prenotazioneHelper) {
        this.prenotazioneHelper = prenotazioneHelper;
    }

    public PrenotazioneDTO findById(Long id){
        Preconditions.checkArgument(!Objects.isNull(id));

        return prenotazioneHelper.findById(id);
    }

    public List<FatturaDTO> findAll(Long idHotel){
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return prenotazioneHelper.findAll(idHotel);
    }

    public List<FatturaDTO> findAllFatture(Long idCliente){
        Preconditions.checkArgument(!Objects.isNull(idCliente));

        return prenotazioneHelper.findAllFatture(idCliente);
    }


    public Boolean delete(Long id){
        Preconditions.checkArgument(!Objects.isNull(id));

        return prenotazioneHelper.delete(id);
    }


    public PrenotazioneDTO create(PrenotazioneDTO prenotazioneDTO){
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataInizio));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataFine));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.idHotel));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.idCliente));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.idStanza));

        return prenotazioneHelper.create(prenotazioneDTO);
    }


    public List<PrenotazioneDTO> findPrenotazionesByStanza_Id(Long idStanza){
        Preconditions.checkArgument(!Objects.isNull(idStanza));

        return prenotazioneHelper.findPrenotazionesByStanza_Id(idStanza);
    }


    public Long update(PrenotazioneDTO prenotazioneDTO){
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.id));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataFine));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataInizio));

        return prenotazioneHelper.update(prenotazioneDTO);
    }

}
