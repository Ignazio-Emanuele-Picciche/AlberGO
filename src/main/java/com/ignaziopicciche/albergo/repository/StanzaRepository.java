package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Stanza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StanzaRepository extends JpaRepository<Stanza, Long> {

    Boolean existsStanzaByNumeroStanzaAndHotel_Id(Integer numeroStanza, Long idHotel);

    List<Stanza> findStanzasByHotel_Id(Long idHotel);

}
