package com.ignaziopicciche.albergo.service;

import com.ignaziopicciche.albergo.dto.CategoriaDTO;

import java.util.List;

public interface CategoriaService {
    Long create(CategoriaDTO categoriaDTO);

    boolean delete(Long id);

    CategoriaDTO findById(Long id);

    Long update(CategoriaDTO categoriaDTO);

    List<CategoriaDTO> findAll(Long idHotel);

    List<CategoriaDTO> findAllByNome(String nome, Long idHotel);
}
