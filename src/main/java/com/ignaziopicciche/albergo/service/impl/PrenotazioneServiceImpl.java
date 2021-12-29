package com.ignaziopicciche.albergo.service.impl;

import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.helper.PrenotazioneHelper;
import com.ignaziopicciche.albergo.service.PrenotazioneService;
import com.stripe.exception.StripeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Nella classe PrenotazioneService sono presenti i metodi che controllano che i dati passati dalla classe
 * PrenotazioneController non siano nulli, in generale controllare che i dati obbligatori non siano nulli o vuoti.
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
public class PrenotazioneServiceImpl implements PrenotazioneService {

    private final PrenotazioneHelper prenotazioneHelper;

    /**
     * In questo metodo viene implementata la logica dell'annotazione @Autowired per l'attributo prenotazioneHelper,
     * ovvero stiamo chiedendo a Spring d'invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe PrenotazioneHelper.
     *
     * @param prenotazioneHelper
     */
    public PrenotazioneServiceImpl(PrenotazioneHelper prenotazioneHelper) {
        this.prenotazioneHelper = prenotazioneHelper;
    }

    @Override
    public PrenotazioneDTO findById(Long id) {
        Preconditions.checkArgument(!Objects.isNull(id));

        return prenotazioneHelper.findById(id);
    }

    @Override
    public List<FatturaDTO> findAll(Long idHotel) {
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return prenotazioneHelper.findAll(idHotel);
    }

    @Override
    public List<FatturaDTO> findAllFatture(Long idCliente) {
        Preconditions.checkArgument(!Objects.isNull(idCliente));

        return prenotazioneHelper.findAllFatture(idCliente);
    }

    @Override
    public Boolean delete(Long id) throws StripeException {
        Preconditions.checkArgument(!Objects.isNull(id));

        return prenotazioneHelper.delete(id);
    }

    @Override
    public PrenotazioneDTO create(PrenotazioneDTO prenotazioneDTO) throws StripeException, ParseException {
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataInizio));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataFine));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.idHotel));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.idCliente));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.idStanza));

        return prenotazioneHelper.create(prenotazioneDTO);
    }

    @Override
    public List<PrenotazioneDTO> findPrenotazionesByStanza_Id(Long idStanza) {
        Preconditions.checkArgument(!Objects.isNull(idStanza));

        return prenotazioneHelper.findPrenotazionesByStanza_Id(idStanza);
    }

    @Override
    public Long update(PrenotazioneDTO prenotazioneDTO) throws ParseException, StripeException {
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.id));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataFine));
        Preconditions.checkArgument(!Objects.isNull(prenotazioneDTO.dataInizio));

        return prenotazioneHelper.update(prenotazioneDTO);
    }

    @Override
    public List<FatturaDTO> findAllByNomeCognomeClienteAndDataInizioAndDataFine(String nomeCliente, String cognomeCliente, String dInizio, String dFine, Long idHotel) throws ParseException {
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        Date dataInizio = StringUtils.isNotBlank(dInizio) ? new SimpleDateFormat("yyyy-MM-dd").parse(dInizio) : null;
        Date dataFine = StringUtils.isNotBlank(dFine) ? new SimpleDateFormat("yyyy-MM-dd").parse(dFine) : null;
        nomeCliente = StringUtils.isNotBlank(nomeCliente) ? nomeCliente : null;
        cognomeCliente = StringUtils.isNotBlank(cognomeCliente) ? cognomeCliente : null;


        return prenotazioneHelper.findAllByNomeCognomeClienteAndDataInizioAndDataFine(nomeCliente, cognomeCliente, dataInizio, dataFine, idHotel);
    }

}
