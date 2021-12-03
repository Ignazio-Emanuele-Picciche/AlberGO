package com.ignaziopicciche.albergo.service;

import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.helper.PrenotazioneHelper;
import com.ignaziopicciche.albergo.model.Prenotazione;
import com.stripe.exception.StripeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PrenotazioneService {

    private final PrenotazioneHelper prenotazioneHelper;

    public PrenotazioneService(PrenotazioneHelper prenotazioneHelper) {
        this.prenotazioneHelper = prenotazioneHelper;
    }

    public PrenotazioneDTO findById(Long id) {
        Preconditions.checkArgument(!Objects.isNull(id));

        return prenotazioneHelper.findById(id);
    }

    public List<FatturaDTO> findAll(Long idHotel) {
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return prenotazioneHelper.findAll(idHotel);
    }

    public List<FatturaDTO> findAllFatture(Long idCliente) {
        Preconditions.checkArgument(!Objects.isNull(idCliente));

        return prenotazioneHelper.findAllFatture(idCliente);
    }


    public Boolean delete(Long id) throws StripeException {
        Preconditions.checkArgument(!Objects.isNull(id));

        return prenotazioneHelper.delete(id);
    }


    public PrenotazioneDTO create(PrenotazioneDTO prenotazioneDTO) throws StripeException, ParseException {
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataInizio));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataFine));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.idHotel));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.idCliente));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.idStanza));

        return prenotazioneHelper.create(prenotazioneDTO);
    }


    public List<PrenotazioneDTO> findPrenotazionesByStanza_Id(Long idStanza) {
        Preconditions.checkArgument(!Objects.isNull(idStanza));

        return prenotazioneHelper.findPrenotazionesByStanza_Id(idStanza);
    }


    public Long update(PrenotazioneDTO prenotazioneDTO) throws ParseException, StripeException {
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.id));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataFine));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataInizio));

        return prenotazioneHelper.update(prenotazioneDTO);
    }


    public List<FatturaDTO> findAllByNomeCognomeClienteAndDataInizioAndDataFine(String nomeCliente, String cognomeCliente, String dInizio, String dFine, Long idHotel) throws ParseException {
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        Date dataInizio = StringUtils.isNotBlank(dInizio) ? new SimpleDateFormat("yyyy-MM-dd").parse(dInizio) : null;
        Date dataFine = StringUtils.isNotBlank(dFine) ? new SimpleDateFormat("yyyy-MM-dd").parse(dFine) : null;
        nomeCliente = StringUtils.isNotBlank(nomeCliente) ? nomeCliente : null;
        cognomeCliente = StringUtils.isNotBlank(cognomeCliente) ? cognomeCliente : null;


        return prenotazioneHelper.findAllByNomeCognomeClienteAndDataInizioAndDataFine(nomeCliente, cognomeCliente, dataInizio, dataFine, idHotel);
    }

}
