package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.HotelDTO;
import com.ignaziopicciche.albergo.exception.HotelException;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HotelHelper {

    @Autowired
    private HotelRepository hotelRepository;


    public HotelDTO create(HotelDTO hotelDTO) {
        if (!hotelRepository.existsByNome(hotelDTO.nome)) {
            Hotel hotel = new Hotel();

            hotel.setNome(hotelDTO.nome);
            hotel.setDescrizione(hotelDTO.descrizione);
            hotel.setIndirizzo(hotelDTO.indirizzo);
            hotel.setStelle(hotelDTO.stelle);
            hotel.setTelefono(hotelDTO.telefono);

            hotelRepository.save(hotel);
            return new HotelDTO(hotel);
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ALREADY_EXISTS);
    }


    public HotelDTO update(HotelDTO hotelDTO) {

        if (hotelRepository.existsById(hotelDTO.id)) {
            Hotel hotel = hotelRepository.findById(hotelDTO.id).get();

            hotel.setDescrizione(hotelDTO.descrizione);
            hotel.setStelle(hotelDTO.stelle);
            hotel.setTelefono(hotelDTO.telefono);

            hotelRepository.save(hotel);
            return new HotelDTO(hotel);
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_NOT_FOUND);
    }


    public HotelDTO findById(Long id) {

        if (hotelRepository.existsById(id)) {
            return new HotelDTO(hotelRepository.findById(id).get());
        }
        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);
    }

}
