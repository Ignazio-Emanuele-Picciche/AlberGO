package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.HotelDTO;
import com.ignaziopicciche.albergo.enums.HotelEnum;
import com.ignaziopicciche.albergo.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.ClienteHotelRepository;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HotelHelper {

    private final HotelRepository hotelRepository;
    private final ClienteRepository clienteRepository;
    private final StripeHelper stripeHelper;
    private final ClienteHotelHelper clienteHotelHelper;
    private final ClienteHotelRepository clienteHotelRepository;

    private static HotelEnum hotelEnum;

    public HotelHelper(HotelRepository hotelRepository, ClienteRepository clienteRepository, StripeHelper stripeHelper, ClienteHotelHelper clienteHotelHelper, ClienteHotelRepository clienteHotelRepository) {
        this.hotelRepository = hotelRepository;
        this.clienteRepository = clienteRepository;
        this.stripeHelper = stripeHelper;
        this.clienteHotelHelper = clienteHotelHelper;
        this.clienteHotelRepository = clienteHotelRepository;
    }


    public HotelDTO create(HotelDTO hotelDTO) throws Exception {
        if (!hotelRepository.existsByNome(hotelDTO.nome)) {

            Hotel hotel = new Hotel();

            hotel.setNome(hotelDTO.nome);
            hotel.setDescrizione(hotelDTO.descrizione);
            hotel.setIndirizzo(hotelDTO.indirizzo);
            hotel.setStelle(hotelDTO.stelle);
            hotel.setTelefono(hotelDTO.telefono);
            hotel.setPublicKey(hotelDTO.publicKey);

            List<Cliente> clienti = clienteRepository.findAll();

            hotel = hotelRepository.save(hotel);

            if(!clienti.isEmpty()){
                for(Cliente cliente: clienti){
                    String customerId = stripeHelper.createCustomer(cliente, hotelDTO.publicKey);
                    clienteHotelHelper.createByCliente(cliente, customerId, hotel);

                    stripeHelper.addClienteHotelCarta(cliente);
                }
            }

            return new HotelDTO(hotel);
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_AE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }


    /*public HotelDTO update(HotelDTO hotelDTO) {

        if (hotelRepository.existsById(hotelDTO.id)) {
            Hotel hotel = hotelRepository.findById(hotelDTO.id).get();

            hotel.setDescrizione(hotelDTO.descrizione);
            hotel.setStelle(hotelDTO.stelle);
            hotel.setTelefono(hotelDTO.telefono);

            hotelRepository.save(hotel);
            return new HotelDTO(hotel);
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_NF");
        throw new ApiRequestException(hotelEnum.getMessage());
    }*/


    public HotelDTO findById(Long id) {

        if (hotelRepository.existsById(id)) {
            return new HotelDTO(hotelRepository.findById(id).get());
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }


    public List<HotelDTO> findHotelByName(String nomeHotel) {
        List<Hotel> hotels = hotelRepository.findHotelByNomeStartingWith(nomeHotel);

        return hotels.stream().map(hotel -> new HotelDTO(hotel)).collect(Collectors.toList());

    }

    public List<HotelDTO> findHotelByIndirizzo(String indirizzoHotel){
        List<Hotel> hotels = hotelRepository.findHotelByIndirizzoStartingWith(indirizzoHotel);
        return hotels.stream().map(hotel -> new HotelDTO(hotel)).collect(Collectors.toList());
    }

}
