package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.model.ClienteHotel;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.ClienteHotelRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClienteHotelHelper {

    private final ClienteHotelRepository clienteHotelRepository;
    private final HotelRepository hotelRepository;

    public ClienteHotelHelper(ClienteHotelRepository clienteHotelRepository, HotelRepository hotelRepository) {
        this.clienteHotelRepository = clienteHotelRepository;
        this.hotelRepository = hotelRepository;
    }

    public void createByCliente(Cliente cliente, String customerId){
        List<Hotel> hotels = hotelRepository.findAll();

        hotels.forEach(hotel ->{
            ClienteHotel clienteHotel = ClienteHotel.builder()
                    .customerId(customerId)
                    .cliente(cliente)
                    .hotel(hotel).build();

            clienteHotelRepository.save(clienteHotel);
        });
    }

}
