package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * L'interfaccia CategoriaRepository estende JpaRepository<T, ID>.
 * Quest'interfaccia, utilizzata nelle classi helper, offre diversi metodi base (es. findAll(), findById() e tanti altri)
 * per relazionarsi con il db.
 * Invece, se si ha bisogno di query SQL native si può utilizzare l’annotazione @Query.
 */

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Boolean existsCategoriaByNomeAndHotel_Id(String name, Long idHotel);

    List<Categoria> findCategoriasByHotel_Id(Long idHotel);

    List<Categoria> findCategoriasByNomeStartingWithAndHotel_Id(String nome, Long idHotel);

}
