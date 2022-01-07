package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.ServizioDTO;
import com.ignaziopicciche.albergo.model.*;
import com.ignaziopicciche.albergo.repository.ClienteHotelRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.repository.PrenotazioneRepository;
import com.ignaziopicciche.albergo.repository.ServizioRepository;
import com.stripe.exception.StripeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ServizioHelperTest {

    ServizioHelper servizioHelper;

    @Mock
    private ServizioRepository servizioRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private PrenotazioneRepository prenotazioneRepository;

    @Mock
    private ClienteHotelRepository clienteHotelRepository;

    @Mock
    private StripeHelper stripeHelper;

    @BeforeEach
    void setUp() {
        servizioHelper = new ServizioHelper(servizioRepository, hotelRepository, prenotazioneRepository, clienteHotelRepository, stripeHelper);
    }

    @Test
    void findServizioByIdTest() {
        Long idServizio = 0L;
        Servizio servizio = new Servizio();

        lenient().when(servizioRepository.existsById(idServizio)).thenReturn(true);
        lenient().when(servizioRepository.findById(idServizio)).thenReturn(Optional.of(servizio));
        assertEquals(servizio, servizioHelper.findById(idServizio));
        verify(servizioRepository, atLeastOnce()).existsById(idServizio);
        verify(servizioRepository, atLeastOnce()).findById(idServizio);
        reset(servizioRepository);
    }

    @Test
    void findAllServiziTest() {
        Long idHotel = 0L;

        Servizio servizio1 = new Servizio();
        Servizio servizio2 = new Servizio();
        Servizio servizio3 = new Servizio();
        List<Servizio> list = List.of(servizio1, servizio2, servizio3);

        lenient().when(hotelRepository.existsById(idHotel)).thenReturn(true);
        lenient().when(servizioRepository.findAllByHotel_Id(idHotel)).thenReturn(list);
        assertEquals(list, servizioHelper.findAll(idHotel));
        verify(servizioRepository, atLeastOnce()).findAllByHotel_Id(idHotel);
        verify(hotelRepository, atLeastOnce()).existsById(idHotel);
        reset(hotelRepository);
        reset(servizioRepository);
    }

    @Test
    void deleteServizioTest() {
        Long idServizio = 0L;

        lenient().when(servizioRepository.existsById(idServizio)).thenReturn(true);
        Assertions.assertTrue(servizioHelper.delete(idServizio));
        verify(servizioRepository, atLeastOnce()).existsById(idServizio);
        reset(servizioRepository);
    }

    @Test
    void updateServizioTest() {
        Long idServizio = 0L;
        String campoUpdate = "prova";

        Servizio updateServizio = Servizio.builder()
                .id(idServizio).build();

        ServizioDTO servizioDTO = ServizioDTO.builder()
                .id(idServizio)
                .nome(campoUpdate).build();

        lenient().when(servizioRepository.existsById(idServizio)).thenReturn(true);
        lenient().when(servizioRepository.findById(idServizio)).thenReturn(Optional.ofNullable(updateServizio));
        assert updateServizio != null;
        lenient().when(servizioRepository.save(updateServizio)).thenReturn(updateServizio);
        assertEquals(idServizio, servizioHelper.update(servizioDTO));
        assertEquals(campoUpdate, updateServizio.getNome());
        verify(servizioRepository, atLeastOnce()).existsById(idServizio);
        verify(servizioRepository, atLeastOnce()).findById(idServizio);
        verify(servizioRepository, atLeastOnce()).save(updateServizio);
        reset(servizioRepository);
    }

    @Test
    void findServiziNotInByPrenotazioneTest() {
        Long idPrenotazione = 0L;
        Long idHotel = 0L;
        Servizio servizio1 = Servizio.builder().id(0L).build();
        Servizio servizio2 = Servizio.builder().id(1L).build();
        Servizio servizio3 = Servizio.builder().id(2L).build();
        Servizio servizio4 = Servizio.builder().id(3L).build();
        Servizio servizio5 = Servizio.builder().id(4L).build();
        List<Servizio> serviziInPrenotazione = List.of(servizio1, servizio2, servizio3);
        Prenotazione prenotazione = Prenotazione.builder()
                .servizi(serviziInPrenotazione)
                .hotel(Hotel.builder().id(idHotel).build()).build();

        List<Servizio> serviziTotali = List.of(servizio1, servizio2, servizio3, servizio4, servizio5);
        List<Servizio> serviziNotInPrenotazione = List.of(servizio4, servizio5);

        lenient().when(prenotazioneRepository.existsById(idPrenotazione)).thenReturn(true);
        lenient().when(prenotazioneRepository.findById(idPrenotazione)).thenReturn(Optional.ofNullable(prenotazione));
        assert prenotazione != null;
        lenient().when(servizioRepository.findAllByHotel_Id(prenotazione.getHotel().getId())).thenReturn(serviziTotali);
        assertEquals(serviziNotInPrenotazione, servizioHelper.findNotInByPrenotazione(idPrenotazione));
        verify(prenotazioneRepository, atLeastOnce()).existsById(idPrenotazione);
        verify(prenotazioneRepository, atLeastOnce()).findById(idPrenotazione);
        verify(servizioRepository, atLeastOnce()).findAllByHotel_Id(prenotazione.getHotel().getId());
        reset(prenotazioneRepository);
        reset(servizioRepository);
    }

    @Test
    void removeServizioInPrenotazioneTest() {
        Long idServizio = 0L;
        Long idPrenotazione = 0L;

        Prenotazione prenotazione = Prenotazione.builder().id(idPrenotazione).build();
        Servizio servizio = new Servizio();
        servizio.addPrenotazione(prenotazione);

        lenient().when(servizioRepository.existsById(idServizio)).thenReturn(true);
        lenient().when(prenotazioneRepository.existsById(idPrenotazione)).thenReturn(true);
        lenient().when(prenotazioneRepository.findById(idPrenotazione)).thenReturn(Optional.of(prenotazione));
        lenient().when(servizioRepository.findById(idPrenotazione)).thenReturn(Optional.of(servizio));
        Assertions.assertTrue(servizioHelper.removeServizioInPrenotazione(idServizio, idPrenotazione));
        Assertions.assertFalse(servizio.getPrenotazioni().contains(prenotazione));
        verify(servizioRepository, atLeastOnce()).existsById(idServizio);
        verify(prenotazioneRepository, atLeastOnce()).existsById(idPrenotazione);
        verify(prenotazioneRepository, atLeastOnce()).findById(idPrenotazione);
        verify(servizioRepository, atLeastOnce()).findById(idServizio);
        reset(prenotazioneRepository);
        reset(servizioRepository);
    }

    @Test
    void findServiziInPrenotazioneTest(){
        Long idPrenotazione = 0L;
        Prenotazione prenotazione = new Prenotazione();
        Servizio servizio1 = new Servizio();
        Servizio servizio2 = new Servizio();
        Servizio servizio3 = new Servizio();
        List<Servizio> list = List.of(servizio1, servizio2, servizio3);
        prenotazione.setServizi(list);

        lenient().when(prenotazioneRepository.existsById(idPrenotazione)).thenReturn(true);
        lenient().when(prenotazioneRepository.findById(idPrenotazione)).thenReturn(Optional.of(prenotazione));
        assertEquals(list, servizioHelper.findServiziInPrenotazione(idPrenotazione));
        verify(prenotazioneRepository, atLeastOnce()).existsById(idPrenotazione);
        verify(prenotazioneRepository, atLeastOnce()).findById(idPrenotazione);
        reset(prenotazioneRepository);
    }

    @Test
    void createServizioTest(){
        Long idServizio = 0L;
        Long idHotel = 0L;
        String nomeServizio = "nomeServizioTest";
        ServizioDTO servizioDTO = ServizioDTO.builder().id(idServizio).idHotel(idHotel).nome(nomeServizio).build();
        Hotel hotel = Hotel.builder().id(idHotel).build();

        lenient().when(servizioRepository.existsByNomeAndHotel_Id(servizioDTO.nome, servizioDTO.idHotel)).thenReturn(false);
        lenient().when(hotelRepository.findById(idHotel)).thenReturn(Optional.ofNullable(hotel));
        Assertions.assertNull(servizioHelper.createServizio(servizioDTO));
        verify(servizioRepository, atLeastOnce()).existsByNomeAndHotel_Id(servizioDTO.nome, servizioDTO.idHotel);
        reset(servizioRepository);
    }


}