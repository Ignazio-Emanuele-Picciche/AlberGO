package com.ignaziopicciche.albergo.service;

import com.google.common.base.Preconditions;
import com.ignaziopicciche.albergo.dto.HotelDTO;
import com.ignaziopicciche.albergo.helper.HotelHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class HotelService {

    @Autowired
    private HotelHelper hotelHelper;

    public HotelDTO create(HotelDTO hotelDTO){
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.indirizzo));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.stelle));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.descrizione));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.telefono));

        return hotelHelper.create(hotelDTO);
    }

    public HotelDTO update(HotelDTO hotelDTO){
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.id));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.stelle));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.descrizione));
        Preconditions.checkArgument(!Objects.isNull(hotelDTO.telefono));

        return hotelHelper.update(hotelDTO);
    }

    public HotelDTO findById(Long id){
        Preconditions.checkArgument(!Objects.isNull(id));

        return hotelHelper.findById(id);
    }

}
