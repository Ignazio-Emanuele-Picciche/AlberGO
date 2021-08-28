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
import java.util.stream.Collectors;

@Component
public class CategoriaHelper {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public CategoriaDTO create(CategoriaDTO categoriaDTO) {

        Optional<Hotel> hotel = hotelRepository.findById(categoriaDTO.idHotel);

        if (!categoriaRepository.existsCategoriaByNomeAndHotel(categoriaDTO.nome, hotel.get()) &&
                !categoriaDTO.nome.equals("") && !categoriaDTO.descrizione.equals("")) {

            Categoria categoria = new Categoria();

            categoria.setNome(categoriaDTO.nome);
            categoria.setDescrizione(categoriaDTO.descrizione);
            categoria.setPrezzo(categoriaDTO.prezzo);
            categoria.setHotel(hotel.get());

            categoriaRepository.save(categoria);
            return new CategoriaDTO(categoria);
        }

        throw new CategoriaException(CategoriaException.CategoriaExcpetionCode.CATEGORIA_ALREADY_EXISTS);

    }

    public CategoriaDTO update(CategoriaDTO categoriaDTO){

        if(categoriaRepository.existsById(categoriaDTO.id)){
            Categoria categoria = categoriaRepository.findById(categoriaDTO.id).get();

            categoria.setId(categoriaDTO.id);
            categoria.setNome(categoriaDTO.nome);
            categoria.setDescrizione(categoriaDTO.descrizione);
            categoria.setPrezzo(categoriaDTO.prezzo);

            categoriaRepository.save(categoria);
            return new CategoriaDTO(categoria);
        }

        throw new CategoriaException(CategoriaException.CategoriaExcpetionCode.CATEGORIA_ID_NOT_EXIST);

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



    public CategoriaDTO findById(Long id){
        if(categoriaRepository.existsById(id)){
            return new CategoriaDTO(categoriaRepository.findById(id).get());
        }

        throw new CategoriaException(CategoriaException.CategoriaExcpetionCode.CATEGORIA_NOT_FOUND);
    }


    public List<CategoriaDTO> findAll(Long idHotel){

        if(hotelRepository.existsById(idHotel)){
            List<Categoria> listCategorie = categoriaRepository.findCategoriasByHotel_Id(idHotel);
            return listCategorie.stream().map(x -> new CategoriaDTO(x)).collect(Collectors.toList());
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);

    }

}
