package com.ignaziopicciche.albergo.service;

import com.ignaziopicciche.albergo.dto.ServizioDTO;
import com.stripe.exception.StripeException;

import java.util.List;

public interface ServizioService {
    Long create(ServizioDTO servizioDTO);
    ServizioDTO findById(Long id);
    List<ServizioDTO> findAll(Long idHotel);
    Boolean delete(Long id);
    Long update(ServizioDTO servizioDTO);
    Long insertByPrentazioneAndHotel(Long idServizio, Long idPrenotazione, Long idHotel) throws StripeException;
    List<ServizioDTO> findNotInByPrenotazione(Long idPrenotazione);
    Boolean removeServizioInPrenotazione(Long idServizio, Long idPrenotazione);
    List<ServizioDTO> findServiziInPrenotazione(Long idPrenotazione);
}
