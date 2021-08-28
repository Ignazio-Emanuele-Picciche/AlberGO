package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
