package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.exception.enums.HotelEnum;
import com.ignaziopicciche.albergo.exception.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class HotelHelperTest {

    HotelHelper hotelHelper;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private StripeHelper stripeHelper;

    @Mock
    private ClienteHotelHelper clienteHotelHelper;

    @BeforeEach
    void setUp() {
        hotelHelper = new HotelHelper(hotelRepository, clienteRepository, stripeHelper, clienteHotelHelper);
    }

    @Test
    void findHotelByIdTest(){
        Long idHotel = 0L;
        Hotel hotel = new Hotel();

        lenient().when(hotelRepository.existsById(idHotel)).thenReturn(true);
        lenient().when(hotelRepository.findById(idHotel)).thenReturn(Optional.of(hotel));
        assertEquals(hotel, hotelHelper.findById(idHotel));
        verify(hotelRepository, atLeastOnce()).existsById(idHotel);
        verify(hotelRepository, atLeastOnce()).findById(idHotel);
        reset(hotelRepository);
    }

    @Test
    void findHotelByNameTest(){
        String nome = "vista";
        Hotel hotel1 = new Hotel();
        Hotel hotel2 = new Hotel();
        Hotel hotel3 = new Hotel();
        List<Hotel> list = List.of(hotel1, hotel2, hotel3);

        lenient().when(hotelRepository.findHotelByNomeStartingWith(nome)).thenReturn(list);
        assertEquals(list, hotelHelper.findHotelByName(nome));
        verify(hotelRepository, atLeastOnce()).findHotelByNomeStartingWith(nome);
        reset(hotelRepository);
    }

    @Test
    void findHotelByIndirizzoTest(){
        String indirizzo = "via macedonia";
        Hotel hotel1 = new Hotel();
        Hotel hotel2 = new Hotel();
        Hotel hotel3 = new Hotel();
        List<Hotel> list = List.of(hotel1, hotel2, hotel3);

        lenient().when(hotelRepository.findHotelByIndirizzoStartingWith(indirizzo)).thenReturn(list);
        assertEquals(list, hotelHelper.findHotelByIndirizzo(indirizzo));
        verify(hotelRepository, atLeastOnce()).findHotelByIndirizzoStartingWith(indirizzo);
        reset(hotelRepository);
    }

    @Test
    void findHotelByCodiceHotelTest(){
        String codiceHotel = "codice123";
        Hotel hotel = new Hotel();

        lenient().when(hotelRepository.existsByCodiceHotel(codiceHotel)).thenReturn(true);
        lenient().when(hotelRepository.findByCodiceHotel(codiceHotel)).thenReturn(hotel);
        assertEquals(hotel, hotelHelper.findHotelByCodiceHotel(codiceHotel));
        verify(hotelRepository, atLeastOnce()).existsByCodiceHotel(codiceHotel);
        verify(hotelRepository, atLeastOnce()).findByCodiceHotel(codiceHotel);
        reset(hotelRepository);
    }

    @Test
    void findAllHoltesTest(){
        Hotel hotel1 = new Hotel();
        Hotel hotel2 = new Hotel();
        Hotel hotel3 = new Hotel();
        List<Hotel> list = List.of(hotel1, hotel2, hotel3);

        lenient().when(hotelRepository.findAll()).thenReturn(list);
        assertEquals(list, hotelHelper.getAllHotel());
        verify(hotelRepository, atLeastOnce()).findAll();
        reset(hotelRepository);
    }

}