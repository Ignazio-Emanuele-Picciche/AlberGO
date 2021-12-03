package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Categoria;
import com.ignaziopicciche.albergo.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Boolean existsCategoriaByNomeAndHotel_Id(String name, Long idHotel);

    List<Categoria> findCategoriasByHotel_Id(Long idHotel);

    List<Categoria> findCategoriasByNomeStartingWithAndHotel_Id(String nome, Long idHotel);

}
