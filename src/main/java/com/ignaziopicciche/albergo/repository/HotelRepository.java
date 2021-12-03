package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Boolean existsByNome(String nome);
    List<Hotel> findHotelByNomeStartingWith(String nomeHotel);
    List<Hotel> findHotelByIndirizzoStartingWith(String indirizzoHotel);
    Hotel findByPublicKey(String key);
}
