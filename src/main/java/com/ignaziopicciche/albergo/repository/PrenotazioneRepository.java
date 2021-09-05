package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.nio.channels.SelectableChannel;
import java.util.Date;
import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    List<Prenotazione> findPrenotazionesByHotel_Id(Long idHotel);


    List<Prenotazione> findPrenotazionesByStanza_IdAndHotel_Id(Long idStanza, Long idHotel);

    Boolean existsByDataInizioAndDataFine(Date dataInizio, Date dataFine);

}
