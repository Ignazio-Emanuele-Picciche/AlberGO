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

    private final CategoriaRepository categoriaRepository;

    private final HotelRepository hotelRepository;

    public CategoriaHelper(CategoriaRepository categoriaRepository, HotelRepository hotelRepository) {
        this.categoriaRepository = categoriaRepository;
        this.hotelRepository = hotelRepository;
    }

    public Long create(CategoriaDTO categoriaDTO) {

        Optional<Hotel> hotel = hotelRepository.findById(categoriaDTO.idHotel);

        if (!categoriaRepository.existsCategoriaByNomeAndHotel(categoriaDTO.nome, hotel.get()) &&
                !categoriaDTO.nome.equals("") && !categoriaDTO.descrizione.equals("")) {

            Categoria categoria = Categoria.builder()
                    .nome(categoriaDTO.nome)
                    .descrizione(categoriaDTO.descrizione)
                    .prezzo(categoriaDTO.prezzo)
                    .giorniPenale(categoriaDTO.giorniPenale)
                    .qtaPenale(categoriaDTO.qtaPenale)
                    .giorniBlocco(categoriaDTO.giorniBlocco)
                    .hotel(hotel.get()).build();


            categoria = categoriaRepository.save(categoria);
            return categoria.getId();
        }

        throw new CategoriaException(CategoriaException.CategoriaExcpetionCode.CATEGORIA_ALREADY_EXISTS);

    }

    public Long update(CategoriaDTO categoriaDTO) {

        if (categoriaRepository.existsById(categoriaDTO.id)) {
            Categoria categoria = categoriaRepository.findById(categoriaDTO.id).get();

            categoria.setDescrizione(categoriaDTO.descrizione);
            categoria.setPrezzo(categoriaDTO.prezzo);
            categoria.setGiorniPenale(categoriaDTO.giorniPenale);
            categoria.setGiorniBlocco(categoriaDTO.giorniBlocco);
            categoria.setQtaPenale(categoriaDTO.qtaPenale);

            categoria = categoriaRepository.save(categoria);
            return categoria.getId();
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


    public CategoriaDTO findById(Long id) {
        if (categoriaRepository.existsById(id)) {
            return new CategoriaDTO(categoriaRepository.findById(id).get());
        }

        throw new CategoriaException(CategoriaException.CategoriaExcpetionCode.CATEGORIA_NOT_FOUND);
    }


    public List<CategoriaDTO> findAll(Long idHotel) {

        if (hotelRepository.existsById(idHotel)) {
            List<Categoria> listCategorie = categoriaRepository.findCategoriasByHotel_Id(idHotel);
            return listCategorie.stream().map(x -> new CategoriaDTO(x)).collect(Collectors.toList());
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);

    }

}
