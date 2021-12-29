package com.ignaziopicciche.albergo.service.impl;

import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.dto.HotelDTO;
import com.ignaziopicciche.albergo.helper.HotelHelper;
import com.ignaziopicciche.albergo.service.HotelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Nella classe HotelService sono presenti i metodi che controllano che i dati passati dalla classe
 * HotelController non siano nulli, in generale controllare che i dati obbligatori non siano nulli o vuoti.
 * Nel caso in cui non fossero nulli, i dati dal livello "service" verranno passati al livello "helper" che si occuperà
 * dell'implementazione della logica, ovvero le operazioni, del metodo.
 * Nel caso in cui, invece, qualche dato obbligatorio non fosse stato compilato, viene restituita un'eccezione nei log
 * del back-end.
 * Per il controllo dei campi viene usato il metodo checkArgument() della classe Preconditions (fornito dalla dependency
 * Guava Preconditions), ponendo il campo obbligatorio diverso da null.
 * <p>
 * In generale:
 * Preconditions.checkArgument(!Objects.isNull("campo obbligatorio"));
 */

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelHelper hotelHelper;

    /**
     * In questo metodo viene implementata la logica dell'annotazione @Autowired per l'attributo hotelHelper,
     * ovvero stiamo chiedendo a Spring d'invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe HotelHelper.
     *
     * @param hotelHelper
     */
    public HotelServiceImpl(HotelHelper hotelHelper) {
        this.hotelHelper = hotelHelper;
    }

    @Override
    public HotelDTO create(HotelDTO hotelDTO) throws Exception {
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.indirizzo));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.stelle));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.descrizione));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.telefono));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.publicKey));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.codiceHotel));

        return hotelHelper.create(hotelDTO);
    }

    @Override
    public HotelDTO findById(Long id) {
        Preconditions.checkArgument(!Objects.isNull(id));

        return hotelHelper.findById(id);
    }

    @Override
    public List<HotelDTO> findHotelByName(String nomeHotel) {
        Preconditions.checkArgument(!Objects.isNull(nomeHotel));

        return hotelHelper.findHotelByName(nomeHotel);
    }

    @Override
    public List<HotelDTO> findHotelByIndirizzo(String indirizzoHotel) {
        Preconditions.checkArgument(!Objects.isNull(indirizzoHotel));

        return hotelHelper.findHotelByIndirizzo(indirizzoHotel);
    }

    @Override
    public HotelDTO findHotelByCodiceHotel(String publicKey) {
        Preconditions.checkArgument(!Objects.isNull(publicKey));

        return hotelHelper.findHotelByCodiceHotel(publicKey);
    }

    @Override
    public List<HotelDTO> getAllHotel() {
        return hotelHelper.getAllHotel();
    }
}
