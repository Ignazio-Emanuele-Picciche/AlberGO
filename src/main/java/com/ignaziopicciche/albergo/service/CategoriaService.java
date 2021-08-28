package com.ignaziopicciche.albergo.service;

import com.google.common.base.Preconditions;
import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import com.ignaziopicciche.albergo.helper.CategoriaHelper;
import com.ignaziopicciche.albergo.model.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaHelper categoriaHelper;

    public Categoria create(CategoriaDTO categoriaDTO, Long idHotel){
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.descrizione));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.prezzo));
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return categoriaHelper.create(categoriaDTO, idHotel);
    }


    public boolean delete(Long id){
        Preconditions.checkArgument(!Objects.isNull(id));

        return categoriaHelper.delete(id);
    }


    public Optional<Categoria> findById(Long id){
        Preconditions.checkArgument(!Objects.isNull(id));

        return categoriaHelper.findById(id);
    }

    public Categoria update(CategoriaDTO categoriaDTO){
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.id));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.descrizione));
        Preconditions.checkArgument(!Objects.isNull(categoriaDTO.prezzo));

        return categoriaHelper.update(categoriaDTO);
    }


    public List<Categoria> findAll(Long idHotel){
        Preconditions.checkArgument(!Objects.isNull(idHotel));

        return categoriaHelper.findAll(idHotel);
    }

}
