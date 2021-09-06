package com.ignaziopicciche.albergo.service;

import com.google.common.base.Preconditions;
import com.ignaziopicciche.albergo.dto.PrenotazioneClienteStanzaCategoriaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.helper.PrenotazioneHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneHelper prenotazioneHelper;

    public PrenotazioneDTO findById(Long id){
        Preconditions.checkArgument(!Objects.isNull(id));

        return prenotazioneHelper.findById(id);
    }

    public List<PrenotazioneClienteStanzaCategoriaDTO> findAll(Long idHotel){
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return prenotazioneHelper.findAll(idHotel);
    }


    public Boolean delete(Long id){
        Preconditions.checkArgument(!Objects.isNull(id));

        return prenotazioneHelper.delete(id);
    }


    public PrenotazioneDTO create(PrenotazioneDTO prenotazioneDTO){
        //Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataInizio));
        //Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataFine));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.idHotel));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.idCliente));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.idStanza));

        return prenotazioneHelper.create(prenotazioneDTO);
    }


    public List<PrenotazioneDTO> findPrenotazionesByStanza_Id(Long idStanza){
        Preconditions.checkArgument(!Objects.isNull(idStanza));

        return prenotazioneHelper.findPrenotazionesByStanza_Id(idStanza);
    }

}
