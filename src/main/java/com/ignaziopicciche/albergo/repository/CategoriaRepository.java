package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("Select c from Categoria c where c.nome = :n and c.hotel = :h")
    Boolean existsByNameAndIdHotel(@Param("n") String name, @Param("h") Long idHotel);


    @Query("select c from Categoria c where c.hotel = :h order by c.id asc")
    List<Categoria> findAllByIdHotel(@Param("h") Long idHotel);

}
