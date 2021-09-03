package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.nio.channels.SelectableChannel;
import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    List<Prenotazione> findPrenotazionesByHotel_Id(Long idHotel);

    @Query("select p from Prenotazione p where p.dataInizio <= CURRENT_DATE and p.dataFine >= CURRENT_DATE " +
            "and p.id = :idPrenotazione")
    Boolean checkDataPrenotazione(Long idPrenotazione);


    @Query("")
    Prenotazione checkStanzaPrenotazione(Long idStanza, Long idPrenotaione);

}
