package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import com.ignaziopicciche.albergo.model.Categoria;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.CategoriaRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoriaHelper {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public Categoria create(CategoriaDTO categoriaDTO){

        if(categoriaRepository.existsByNameAndIdHotel(categoriaDTO.nome, categoriaDTO.idHotel) &&
                !!categoriaDTO.nome.equals("") && !!categoriaDTO.descrizione.equals("")){

            Categoria categoria = new Categoria();
            Optional<Hotel> hotel = Optional.of(new Hotel());

            hotel = hotelRepository.findById(categoriaDTO.idHotel);

            categoria.setNome(categoriaDTO.nome);
            categoria.setDescrizione(categoriaDTO.descrizione);
            categoria.setPrezzo(categoriaDTO.prezzo);
            categoria.setHotel(hotel.get());

            return categoriaRepository.save(categoria);
        }

        //throw new CategoriaException(CategoriaException.CategoriaExceptionCode.CATEGORY_ALREADY_EXISTS);

    }
}
