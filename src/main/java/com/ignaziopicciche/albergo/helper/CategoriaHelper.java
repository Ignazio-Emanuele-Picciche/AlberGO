package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import com.ignaziopicciche.albergo.exception.enums.CategoriaEnum;
import com.ignaziopicciche.albergo.exception.enums.HotelEnum;
import com.ignaziopicciche.albergo.exception.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.Categoria;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.CategoriaRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * La classe CategoriaHelper contiene i metodi che si occupano dell'implementazione delle logiche
 * e funzionalità vere e proprie degli endpoint richiamati dal front-end. I dati che vengono forniti a questi metodi
 * provengono dal livello "service" nel quale è stato controllato che i campi obbligatori sono stati inseriti correttamente
 * nel front-end.
 * Per "logiche e funzionalita" si intende:
 * -comunicazioni con il livello "repository" che si occuperà delle operazioni CRUD e non solo:
 * -es. controllare che una categoria è gia presente nel sistema;
 * -es. aggiungere, eliminare, cercare, aggiornare una categoria.
 * -varie operazioni di logica (calcoli, operazioni, controlli generici)
 * -restituire, al front-end, le eccezioni custom in caso di errore (es. La categoria che vuoi inserire è già presente nel sistema)
 * -in caso di operazioni andate a buon fine, verranno restituiti al livello service i dati che dovranno essere inviati al front-end.
 */

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

    /**
     * Metodo che si occupa di controllare se la categoria che si vuole aggiungere non è presente nel sistema.
     * In caso positivo la categoria verrà inserito nel database di sistema tramite il livello "repository".
     * In caso negativo verrà restituita un'eccezione custom al front-end (La categoria che si vuole aggiungere è presente nel sistema).
     *
     * @param categoriaDTO
     * @return Categoria
     */
    public Categoria create(CategoriaDTO categoriaDTO) {

        Hotel hotel = hotelRepository.findById(categoriaDTO.idHotel).get();

        if (categoriaDTO.giorniBlocco >= categoriaDTO.giorniPenale && categoriaDTO.giorniPenale > 0) {
            categoriaEnum = CategoriaEnum.getCategoriaEnumByMessageCode("CAT_CRE");
            throw new ApiRequestException(categoriaEnum.getMessage());
        }

        if (!categoriaRepository.existsCategoriaByNomeAndHotel_Id(categoriaDTO.nome, hotel.getId()) &&
                !categoriaDTO.nome.equals("") && !categoriaDTO.descrizione.equals("")) {


            Categoria categoria = Categoria.builder()
                    .nome(categoriaDTO.nome)
                    .descrizione(categoriaDTO.descrizione)
                    .prezzo(categoriaDTO.prezzo)
                    .giorniPenale(categoriaDTO.giorniPenale)
                    .qtaPenale(categoriaDTO.qtaPenale)
                    .giorniBlocco(categoriaDTO.giorniBlocco)
                    .hotel(hotel).build();


            categoria = categoriaRepository.save(categoria);
            return categoria;
        }

        categoriaEnum = CategoriaEnum.getCategoriaEnumByMessageCode("CAT_AE");
        throw new ApiRequestException(categoriaEnum.getMessage());
    }

    /**
     * Metodo che controlla se la categoria che si vuole aggiornare è presente nel sistema.
     * In caso positivo verranno aggiornati solo i campi "editabili".
     * In caso negatio verrà restituita un'eccezione custom al front-end (La categoria che stai cercando non esiste)
     *
     * @param categoriaDTO
     * @return idCategoria
     */
    public Long update(CategoriaDTO categoriaDTO) {

        if (categoriaDTO.giorniBlocco >= categoriaDTO.giorniPenale && categoriaDTO.giorniPenale > 0) {
            categoriaEnum = CategoriaEnum.getCategoriaEnumByMessageCode("CAT_UPE");
            throw new ApiRequestException(categoriaEnum.getMessage());
        }

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

    /**
     * Metodo che controlla se la categoria che si vuole eliminare è presente nel sistema.
     * In caso positivo verrà eliminata dal sistema
     * In caso negati verrà restituita un'eccezione custom al front-end (Errore durante l'eliminazione della categoria)
     *
     * @param id
     * @return Boolean
     */
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

    /**
     * Metodo che si occupa di controllare se la categoria che si sta cercando è presente nel sistema.
     * In caso positivo verra cercata e restituita la categoria tramite il suo idCategoria associato
     * In caso negativo verrà restituita un'eccezione custom (La categoria che stai cercando non è stata trovata)
     *
     * @param id
     * @return Categoria
     */
    public Categoria findById(Long id) {
        if (categoriaRepository.existsById(id)) {
            return categoriaRepository.findById(id).get();
        }

        categoriaEnum = CategoriaEnum.getCategoriaEnumByMessageCode("CAT_NF");
        throw new ApiRequestException(categoriaEnum.getMessage());
    }


    /**
     * Metodo che controlla se l'hotel, per il quale si vogliono restituire tutte le categorie, esiste.
     * In caso positivo restituisce tutte le categoria dell'hotel
     * In caso negativo restituisce un'eccezione custom (L'hotel che stai cercando non esiste)
     *
     * @param idHotel
     * @return List<Categoria>
     */
    public List<Categoria> findAll(Long idHotel) {
        if (hotelRepository.existsById(idHotel)) {
            return categoriaRepository.findCategoriasByHotel_Id(idHotel);
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    /**
     * Metodo che cerca tutte le categorie dell'hotel che iniziano per nome
     *
     * @param nome
     * @param idHotel
     * @return List<Categoria>
     */
    public List<Categoria> findAllByNome(String nome, Long idHotel) {
        return categoriaRepository.findCategoriasByNomeStartingWithAndHotel_Id(nome, idHotel);
    }
}
