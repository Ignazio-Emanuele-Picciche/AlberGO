package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import com.ignaziopicciche.albergo.exception.CategoriaException;
import com.ignaziopicciche.albergo.exception.HotelException;
import com.ignaziopicciche.albergo.model.Categoria;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.CategoriaRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoriaHelper {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public Categoria create(CategoriaDTO categoriaDTO) {

        if (categoriaRepository.existsByNameAndIdHotel(categoriaDTO.nome, categoriaDTO.idHotel) &&
                !!categoriaDTO.nome.equals("") && !!categoriaDTO.descrizione.equals("")) {

            Categoria categoria = new Categoria();
            Optional<Hotel> hotel = Optional.of(new Hotel());

            hotel = hotelRepository.findById(categoriaDTO.idHotel);

            categoria.setNome(categoriaDTO.nome);
            categoria.setDescrizione(categoriaDTO.descrizione);
            categoria.setPrezzo(categoriaDTO.prezzo);
            categoria.setHotel(hotel.get());

            return categoriaRepository.save(categoria);
        }

        throw new CategoriaException(CategoriaException.CategoriaExcpetionCode.CATEGORIA_ALREADY_EXISTS);

    }

    public Boolean delete(Long id) {

        if (categoriaRepository.existsById(id)) {
            try {
                categoriaRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                throw new CategoriaException(CategoriaException.CategoriaExcpetionCode.CATEGORIA_DELETE_ERROR);
            }
        }

        throw new CategoriaException(CategoriaException.CategoriaExcpetionCode.CATEGORIA_ID_NOT_EXIST);
    }


    public Optional<Categoria> findById(Long id){
        if(categoriaRepository.existsById(id)){
            return categoriaRepository.findById(id);
        }

        throw new CategoriaException(CategoriaException.CategoriaExcpetionCode.CATEGORIA_NOT_FOUND);
    }


    public Categoria update(CategoriaDTO categoriaDTO){

        if(categoriaRepository.existsById(categoriaDTO.id)){
            Categoria categoria = new Categoria();
            categoria.setId(categoriaDTO.id);
            categoria.setNome(categoriaDTO.nome);
            categoria.setDescrizione(categoriaDTO.descrizione);
            categoria.setPrezzo(categoriaDTO.prezzo);

            return categoriaRepository.save(categoria);
        }

        throw new CategoriaException(CategoriaException.CategoriaExcpetionCode.CATEGORIA_ID_NOT_EXIST);

    }


    public List<Categoria> findAll(Long idHotel){

        if(hotelRepository.existsById(idHotel)){
            return categoriaRepository.findAllByIdHotel(idHotel);
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);

    }













}
