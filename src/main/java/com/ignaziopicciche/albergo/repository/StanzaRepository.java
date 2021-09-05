package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Stanza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface StanzaRepository extends JpaRepository<Stanza, Long> {

    Boolean existsStanzaByNumeroStanzaAndHotel_Id(Integer numeroStanza, Long idHotel);

    List<Stanza> findStanzasByHotel_Id(Long idHotel);

    //FindStanzasByCategoria_IdAndHotel_IdAndDataInizioAndDataFine
    @Query("select s from Stanza s join Prenotazione p on s.id = p.stanza.id where p.dataInizio = :i and " +
            "p.dataFine = :f and s.categoria.id = :c and s.hotel.id = :h")
    List<Stanza> findStanzasByCategoria_IdAndHotel_IdAndDates(@Param("c") Long idCategoria, @Param("h") Long idHotel, @Param("i") Date dataInizio, @Param("f") Date dataFine);


    //FindStanzasFuoriServizioByDataInizioAndDataFineAAndHotel_Id
    @Query("select s from Stanza s join Prenotazione p on s.id = p.stanza.id where p.dataInizio = :i and " +
            "p.dataFine = :f and s.hotel.id = :h and s.fuoriServizio = true ")
    List<Stanza> findStanzasFuoriServizioByHotel_IDAndDates(@Param("h") Long idHotel, @Param("i") Date dataInizio, @Param("f") Date dataFine);

    //FindStanzasLibereByDataInizioAndDataFineAAndHotel_Id
    @Query("select s from Stanza s where s.hotel.id = :h and s not in (select s1 from Stanza s1 join Prenotazione p on" +
            "s1.id = p.stanza.id where p.dataInizio = :i and p.dataFine = :f)")
    List<Stanza> findStanzasLibereByHotel_IdAndDates(@Param("h") Long idHotel, @Param("i") Date dataInizio, @Param("f") Date dataFine);


    //FindStanzasByDataInizioAndDataFineAndHotel_Id
    @Query("select s from Stanza s join Prenotazione p on s.id = p.stanza.id where s.hotel.id = :h and " +
            "p.dataInizio = :i and p.dataFine = :f")
    List<Stanza> FindStanzasByHotel_IdAndDates(@Param("h") Long idHotel, @Param("i") Date dataInizio, @Param("f") Date dataFine);

}
