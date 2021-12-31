package com.ignaziopicciche.albergo.service.impl;

import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.dto.ServizioDTO;
import com.ignaziopicciche.albergo.helper.ServizioHelper;
import com.ignaziopicciche.albergo.service.ServizioService;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Nella classe ServizioService sono presenti i metodi che controllano che i dati passati dalla classe
 * ServizioController non siano nulli, in generale controllare che i dati obbligatori non siano nulli o vuoti.
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
public class ServizioServiceImpl implements ServizioService {

    private final ServizioHelper servizioHelper;

    /**
     * In questo metodo viene implementata la logica dell'annotazione @Autowired per l'attributo servizioHelper,
     * ovvero stiamo chiedendo a Spring d'invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe ServizioHelper.
     *
     * @param servizioHelper
     */
    public ServizioServiceImpl(ServizioHelper servizioHelper) {
        this.servizioHelper = servizioHelper;
    }

    @Override
    public Long create(ServizioDTO servizioDTO) {
        Preconditions.checkArgument(!Objects.isNull(servizioDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(servizioDTO.prezzo));
        Preconditions.checkArgument(!Objects.isNull(servizioDTO.idHotel));

        return servizioHelper.create(servizioDTO);
    }

    @Override
    public ServizioDTO findById(Long id) {
        Preconditions.checkArgument(!Objects.isNull(id));

        return new ServizioDTO(servizioHelper.findById(id));
    }

    @Override
    public List<ServizioDTO> findAll(Long idHotel) {
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return servizioHelper.findAll(idHotel).stream().map(ServizioDTO::new).collect(Collectors.toList());
    }

    @Override
    public Boolean delete(Long id) {
        Preconditions.checkArgument(!Objects.isNull(id));

        return servizioHelper.delete(id);
    }

    @Override
    public Long update(ServizioDTO servizioDTO) {
        Preconditions.checkArgument(!Objects.isNull(servizioDTO.id));
        Preconditions.checkArgument(!Objects.isNull(servizioDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(servizioDTO.prezzo));

        return servizioHelper.update(servizioDTO);
    }

    @Override
    public Long insertByPrentazioneAndHotel(Long idServizio, Long idPrenotazione, Long idHotel) throws StripeException {
        Preconditions.checkArgument(!Objects.isNull(idPrenotazione));
        Preconditions.checkArgument(!Objects.isNull(idServizio));
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return servizioHelper.insertByPrentazioneAndHotel(idServizio, idPrenotazione, idHotel);
    }

    @Override
    public List<ServizioDTO> findNotInByPrenotazione(Long idPrenotazione) {
        Preconditions.checkArgument(!Objects.isNull(idPrenotazione));

        return servizioHelper.findNotInByPrenotazione(idPrenotazione).stream().map(ServizioDTO::new).collect(Collectors.toList());
    }

    @Override
    public Boolean removeServizioInPrenotazione(Long idServizio, Long idPrenotazione) {
        Preconditions.checkArgument(!Objects.isNull(idServizio));
        Preconditions.checkArgument(!Objects.isNull(idPrenotazione));

        return servizioHelper.removeServizioInPrenotazione(idServizio, idPrenotazione);
    }

    @Override
    public List<ServizioDTO> findServiziInPrenotazione(Long idPrenotazione) {
        Preconditions.checkArgument(!Objects.isNull(idPrenotazione));

        return servizioHelper.findServiziInPrenotazione(idPrenotazione).stream().map(ServizioDTO::new).collect(Collectors.toList());
    }
}
