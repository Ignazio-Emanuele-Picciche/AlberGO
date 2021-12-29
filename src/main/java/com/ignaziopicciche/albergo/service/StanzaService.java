package com.ignaziopicciche.albergo.service;

import com.ignaziopicciche.albergo.dto.StanzaDTO;

import java.util.Date;
import java.util.List;

public interface StanzaService {
    StanzaDTO create(StanzaDTO stanzaDTO);
    Boolean delete(Long id);
    StanzaDTO update(StanzaDTO stanzaDTO);
    StanzaDTO findById(Long id);
    List<StanzaDTO> findAll(Long idHotel);
    List<StanzaDTO> findStanzasByCategoria_IdAndDates(Long idCategoria, Date dataInizio, Date dataFine);
    int findCountStanzasFuoriServizioByHotel_Id(Long idHotel);
    List<StanzaDTO> findStanzasLibereByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine);
    List<StanzaDTO> findStanzasOccupateByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine);
    int findCountStanzeByCategoria_Id(Long idCategoria);
}
