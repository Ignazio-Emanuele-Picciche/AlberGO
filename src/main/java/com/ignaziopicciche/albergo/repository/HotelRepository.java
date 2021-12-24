package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * L'interfaccia HotelRepository estende JpaRepository<T, ID>.
 * Quest'interfaccia, utilizzata nelle classi helper, offre diversi metodi base (es. findAll(), findById() e tanti altri)
 * per relazionarsi con il db.
 * Invece, se si ha bisogno di query SQL native si può utilizzare l’annotazione @Query.
 */

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Boolean existsByNomeOrCodiceHotel(String nome, String codiceHotel);
    List<Hotel> findHotelByNomeStartingWith(String nomeHotel);
    List<Hotel> findHotelByIndirizzoStartingWith(String indirizzoHotel);
    Hotel findByCodiceHotel(String codiceHotel);
    Boolean existsByCodiceHotel(String codiceHotel);
}
