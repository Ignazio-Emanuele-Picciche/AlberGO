package com.ignaziopicciche.albergo.service;

import com.ignaziopicciche.albergo.dto.HotelDTO;

import java.util.List;

public interface HotelService {
    HotelDTO create(HotelDTO hotelDTO) throws Exception;

    HotelDTO findById(Long id);

    List<HotelDTO> findHotelByName(String nomeHotel);

    List<HotelDTO> findHotelByIndirizzo(String indirizzoHotel);

    HotelDTO findHotelByCodiceHotel(String publicKey);

    List<HotelDTO> getAllHotel();
}
