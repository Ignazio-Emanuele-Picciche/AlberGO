package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.ClienteHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * L'interfaccia ClienteHotelRepository estende JpaRepository<T, ID>.
 * Quest'interfaccia, utilizzata nelle classi helper, offre diversi metodi base (es. findAll(), findById() e tanti altri)
 * per relazionarsi con il db.
 * Invece, se si ha bisogno di query SQL native si può utilizzare l’annotazione @Query.
 */

public interface ClienteHotelRepository extends JpaRepository<ClienteHotel, Long> {

    ClienteHotel findByCliente_IdAndHotel_Id(Long idCliente, Long idHotel);
    List<ClienteHotel> findByCliente_Id(Long idCliente);

    void deleteAllByCliente_Id(@Param("idCliente") Long idCliente);

}
