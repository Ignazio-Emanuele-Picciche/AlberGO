package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Categoria;
import com.ignaziopicciche.albergo.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Boolean existsCategoriaByNomeAndHotel(String name, Hotel hotel);

    List<Categoria> findCategoriasByHotel_Id(Long idHotel);

}
