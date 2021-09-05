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
    @Query("select s from Stanza s where s.categoria.id = :c and s.id not in " +
            "(select p.stanza.id from Prenotazione p where s.id = p.stanza.id and " +
            "(:i between p.dataInizio and p.dataFine) or" +
            "(:f between p.dataInizio and p.dataFine) or" +
            ":i <= p.dataInizio and :f >= p.dataFine)")
    List<Stanza> findStanzasByCategoria_IdAndDates(@Param("c") Long idCategoria, @Param("i") Date dataInizio, @Param("f") Date dataFine);



    //findCountStanzasFuoriServizioByHotel_Id
    @Query("select count(s) from Stanza s where s.hotel.id = :h and s.fuoriServizio = true")
    int findCountStanzasFuoriServizioByHotel_Id(@Param("h") Long idHotel);


    //findStanzasLibereByHotel_IdAndDates -> come sopra -> ritorna la lista
    @Query("select s from Stanza s where s.hotel.id = :h and s.id not in" +
            "(select p.stanza.id from Prenotazione p where s.id = p.stanza.id and" +
            "(:i between p.dataInizio and p.dataFine) or" +
            "(:f between p.dataInizio and p.dataFine) or" +
            ":i <= p.dataInizio and :f >= p.dataFine)")
    List<Stanza> findStanzasLibereByHotel_IdAndDates(@Param("h") Long idHotel, @Param("i") Date dataInizio, @Param("f") Date dataFine);


    //findStanzasOccupateByHotel_IdAndDates
    @Query("select s from Stanza s where s.hotel.id = :h and s.id in" +
            "(select p.stanza.id from Prenotazione p where s.id = p.stanza.id and" +
            "(:i between p.dataInizio and p.dataFine) or" +
            "(:f between p.dataInizio and p.dataFine) or" +
            ":i <= p.dataInizio and :f >= p.dataFine)")
    List<Stanza> findStanzasOccupateByHotel_IdAndDates(@Param("h") Long idHotel, @Param("i") Date dataInizio, @Param("f") Date dataFine);
}
