package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.enums.CategoriaEnum;
import com.ignaziopicciche.albergo.enums.HotelEnum;
import com.ignaziopicciche.albergo.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.Categoria;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.CategoriaRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoriaHelper {

    private final CategoriaRepository categoriaRepository;

    private final HotelRepository hotelRepository;

    private static CategoriaEnum categoriaEnum;
    private static HotelEnum hotelEnum;

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

        categoriaEnum = CategoriaEnum.getCategoriaEnumByMessageCode("CAT_AE");
        throw new ApiRequestException(categoriaEnum.getMessage());

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

        categoriaEnum = CategoriaEnum.getCategoriaEnumByMessageCode("CAT_IDNE");
        throw new ApiRequestException(categoriaEnum.getMessage());

    }

    public Boolean delete(Long id) {

        if (categoriaRepository.existsById(id)) {
            try {
                categoriaRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                categoriaEnum = CategoriaEnum.getCategoriaEnumByMessageCode("CAT_DLE");
                throw new ApiRequestException(categoriaEnum.getMessage());
            }
        }


        categoriaEnum = CategoriaEnum.getCategoriaEnumByMessageCode("CAT_IDNE");
        throw new ApiRequestException(categoriaEnum.getMessage());
    }


    public CategoriaDTO findById(Long id) {
        if (categoriaRepository.existsById(id)) {
            return new CategoriaDTO(categoriaRepository.findById(id).get());
        }

        categoriaEnum = CategoriaEnum.getCategoriaEnumByMessageCode("CAT_NF");
        throw new ApiRequestException(categoriaEnum.getMessage());
    }


    public List<CategoriaDTO> findAll(Long idHotel) {

        if (hotelRepository.existsById(idHotel)) {
            List<Categoria> listCategorie = categoriaRepository.findCategoriasByHotel_Id(idHotel);
            return listCategorie.stream().map(x -> new CategoriaDTO(x)).collect(Collectors.toList());
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }


    public List<CategoriaDTO> findAllByNome(String nome) {
        List<Categoria> categorie = categoriaRepository.findCategoriasByNomeStartingWith(nome);
        return categorie.stream().map(categoria -> new CategoriaDTO(categoria)).collect(Collectors.toList());
    }
}
