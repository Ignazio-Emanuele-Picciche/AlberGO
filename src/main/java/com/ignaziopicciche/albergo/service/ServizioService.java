package com.ignaziopicciche.albergo.service;

import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.dto.ServizioDTO;
import com.ignaziopicciche.albergo.helper.ServizioHelper;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Service
public class ServizioService {

    private final ServizioHelper servizioHelper;

    public ServizioService(ServizioHelper servizioHelper) {
        this.servizioHelper = servizioHelper;
    }


    public Long create(ServizioDTO servizioDTO){
        Preconditions.checkArgument(!Objects.isNull(servizioDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(servizioDTO.prezzo));
        Preconditions.checkArgument(!Objects.isNull(servizioDTO.idHotel));

        return servizioHelper.create(servizioDTO);
    }


    public ServizioDTO findById(Long id) {
        Preconditions.checkArgument(!Objects.isNull(id));

        return servizioHelper.findById(id);
    }

    public List<ServizioDTO> findAll(Long idHotel){
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return servizioHelper.findAll(idHotel);
    }


    public Boolean delete(Long id) {
        Preconditions.checkArgument(!Objects.isNull(id));

        return servizioHelper.delete(id);
    }

    public Long update(ServizioDTO servizioDTO) {
        Preconditions.checkArgument(!Objects.isNull(servizioDTO.id));
        Preconditions.checkArgument(!Objects.isNull(servizioDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(servizioDTO.prezzo));

        return servizioHelper.update(servizioDTO);
    }

    public Long insertByPrentazioneAndHotel(Long idServizio, Long idPrenotazione, Long idHotel) throws StripeException {
        Preconditions.checkArgument(!Objects.isNull(idPrenotazione));
        Preconditions.checkArgument(!Objects.isNull(idServizio));
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return servizioHelper.insertByPrentazioneAndHotel(idServizio, idPrenotazione, idHotel);
    }


    public List<ServizioDTO> findNotInByPrenotazione(Long idPrenotazione){
        Preconditions.checkArgument(!Objects.isNull(idPrenotazione));

        return servizioHelper.findNotInByPrenotazione(idPrenotazione);
    }


    public Boolean removeServizioInPrenotazione(Long idServizio, Long idPrenotazione) {
        Preconditions.checkArgument(!Objects.isNull(idServizio));
        Preconditions.checkArgument(!Objects.isNull(idPrenotazione));

        return servizioHelper.removeServizioInPrenotazione(idServizio, idPrenotazione);
    }


    public List<ServizioDTO> findServiziInPrenotazione(Long idPrenotazione) {
        Preconditions.checkArgument(!Objects.isNull(idPrenotazione));

        return servizioHelper.findServiziInPrenotazione(idPrenotazione);
    }
}
