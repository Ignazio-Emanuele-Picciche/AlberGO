package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Servizio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * L'interfaccia ServizioRepository estende JpaRepository<T, ID>.
 * Quest'interfaccia, utilizzata nelle classi helper, offre diversi metodi base (es. findAll(), findById() e tanti altri)
 * per relazionarsi con il db.
 * Invece, se si ha bisogno di query SQL native si può utilizzare l’annotazione @Query.
 */

public interface ServizioRepository extends JpaRepository<Servizio, Long> {

    Boolean existsByNome(String nome);
    List<Servizio> findAllByHotel_Id(Long idHotel);

    Boolean existsServizioByIdAndHotel_Id(Long idServizio, Long idHotel);

}
