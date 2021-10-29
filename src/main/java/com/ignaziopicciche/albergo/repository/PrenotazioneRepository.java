package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.model.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.nio.channels.SelectableChannel;
import java.util.Date;
import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    List<Prenotazione> findPrenotazionesByCliente_Id(Long idCliente);
    List<Prenotazione> findPrenotazionesByHotel_Id(Long idHotel);

    List<Prenotazione> findPrenotazionesByStanza_IdAndHotel_Id(Long idStanza, Long idHotel);

    @Query("select count(p) from Prenotazione p where p.stanza.id = :idStanza and ((:i between p.dataInizio and p.dataFine) or" +
            "(:f between p.dataInizio and p.dataFine) or" +
            ":i <= p.dataInizio and :f >= p.dataFine)")
    int checkPrenotazioneDate(@Param("i") Date dataInizio, @Param("f") Date dataFine, @Param("idStanza") Long idStanza);

    @Query("select count(p) from Prenotazione p where ((:dataInizio between p.dataInizio and p.dataFine) or" +
            "(:dataFine between p.dataInizio and p.dataFine) or" +
            ":dataInizio <= p.dataInizio and :dataFine >= p.dataFine) and p.id <> :id and p.stanza.id = :idStanza ")
    int checkPrenotazioneDateUpdate(@Param("dataInizio") Date dataInizio, @Param("dataFine") Date dataFine, @Param("id") Long idPrenotazione, @Param("idStanza") Long idStanza);

    List<Prenotazione> findPrenotazionesByStanza_Id(Long idStanza);


    List<Prenotazione> findPrenotazionesByCliente_NomeStartingWithAndHotel_Id(String nomeCliente, Long idHotel);
    List<Prenotazione> findPrenotazionesByCliente_CognomeStartingWithAndHotel_Id(String cognomeCliente, Long idHotel);

    @Query("select distinct (p) from Prenotazione p where p.cliente.nome like :nomeCliente% and p.hotel.id = :idHotel and " +
            "((:dataInizio between p.dataInizio and p.dataFine) or" +
            "(:dataFine between p.dataInizio and p.dataFine) or" +
            ":dataInizio <= p.dataInizio and :dataFine >= p.dataFine)")
    List<Prenotazione> findAllByNomeClienteAndDataInizioAndDataFine(@Param("nomeCliente") String nomeCliente, @Param("dataInizio") Date dataInizio, @Param("dataFine") Date dataFine, @Param("idHotel") Long idHotel);

    @Query("select distinct (p) from Prenotazione p where p.cliente.cognome like :cognomeCliente% and p.hotel.id = :idHotel and" +
            "((:dataInizio between p.dataInizio and p.dataFine) or" +
            "(:dataFine between p.dataInizio and p.dataFine) or" +
            ":dataInizio <= p.dataInizio and :dataFine >= p.dataFine)")
    List<Prenotazione> findAllByCognomeClienteAndDataInizioAndDataFine(@Param("cognomeCliente") String cognomeCliente, @Param("dataInizio") Date dataInizio, @Param("dataFine") Date dataFine, @Param("idHotel") Long idHotel);

    @Query("select distinct (p) from Prenotazione p where p.hotel.id = :idHotel and" +
            "((:dataInizio between p.dataInizio and p.dataFine) or" +
            "(:dataFine between p.dataInizio and p.dataFine) or" +
            ":dataInizio <= p.dataInizio and :dataFine >= p.dataFine)")
    List<Prenotazione> findAllByDataInizioAndDataFine(@Param("dataInizio") Date dataInizio, @Param("dataFine") Date dataFine, @Param("idHotel") Long idHotel);


    Boolean existsPrenotazioneByIdAndHotel_Id(Long idPrenotazione, Long idHotel);
}
