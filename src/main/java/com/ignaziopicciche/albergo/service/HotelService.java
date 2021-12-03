package com.ignaziopicciche.albergo.service;

import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.dto.HotelDTO;
import com.ignaziopicciche.albergo.helper.HotelHelper;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class HotelService {

    private final HotelHelper hotelHelper;

    public HotelService(HotelHelper hotelHelper) {
        this.hotelHelper = hotelHelper;
    }


    public HotelDTO create(HotelDTO hotelDTO) throws Exception {
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.indirizzo));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.stelle));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.descrizione));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.telefono));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.publicKey));

        return hotelHelper.create(hotelDTO);
    }

    /*public HotelDTO update(HotelDTO hotelDTO) {
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.id));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.stelle));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.descrizione));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.telefono));

        return hotelHelper.update(hotelDTO);
    }*/

    public HotelDTO findById(Long id) {
        Preconditions.checkArgument(!Objects.isNull(id));

        return hotelHelper.findById(id);
    }


    public List<HotelDTO> findHotelByName(String nomeHotel) {
        Preconditions.checkArgument(!Objects.isNull(nomeHotel));

        return hotelHelper.findHotelByName(nomeHotel);
    }

    public List<HotelDTO> findHotelByIndirizzo(String indirizzoHotel){
        Preconditions.checkArgument(!Objects.isNull(indirizzoHotel));

        return hotelHelper.findHotelByIndirizzo(indirizzoHotel);
    }

    public List<HotelDTO> getAllHotel(){
        return hotelHelper.getAllHotel();
    }
}
