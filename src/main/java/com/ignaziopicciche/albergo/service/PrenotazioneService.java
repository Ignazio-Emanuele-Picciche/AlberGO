package com.ignaziopicciche.albergo.service;

import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.stripe.exception.StripeException;

import java.text.ParseException;
import java.util.List;

public interface PrenotazioneService {
    PrenotazioneDTO findById(Long id);

    List<FatturaDTO> findAll(Long idHotel);

    List<FatturaDTO> findAllFatture(Long idCliente);

    Boolean delete(Long id) throws StripeException;

    PrenotazioneDTO create(PrenotazioneDTO prenotazioneDTO) throws StripeException, ParseException;

    List<PrenotazioneDTO> findPrenotazionesByStanza_Id(Long idStanza);

    Long update(PrenotazioneDTO prenotazioneDTO) throws ParseException, StripeException;

    List<FatturaDTO> findAllByNomeCognomeClienteAndDataInizioAndDataFine(String nomeCliente, String cognomeCliente, String dInizio, String dFine, Long idHotel) throws ParseException;
}
