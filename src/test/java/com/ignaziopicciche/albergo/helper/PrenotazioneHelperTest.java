package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.model.*;
import com.ignaziopicciche.albergo.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PrenotazioneHelperTest {

    PrenotazioneHelper prenotazioneHelper;

    @Mock
    private PrenotazioneRepository prenotazioneRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private StanzaRepository stanzaRepository;

    @Mock
    private ClienteHotelRepository clienteHotelRepository;

    @Mock
    private StripeHelper stripeHelper;

    @BeforeEach
    void setUp() {
        prenotazioneHelper = new PrenotazioneHelper(prenotazioneRepository, hotelRepository, clienteRepository, stanzaRepository, clienteHotelRepository, stripeHelper);
    }

    @Test
    void findPrenotazioneByIdTest() {
        Long idPrenotazione = 0L;
        Prenotazione prenotazione = Prenotazione.builder()
                .id(idPrenotazione).build();

        lenient().when(prenotazioneRepository.existsById(idPrenotazione)).thenReturn(true);
        lenient().when(prenotazioneRepository.findById(idPrenotazione)).thenReturn(Optional.ofNullable(prenotazione));
        assertEquals(prenotazione, prenotazioneHelper.findPrenotazioneById(idPrenotazione));
        verify(prenotazioneRepository, atLeastOnce()).existsById(idPrenotazione);
        verify(prenotazioneRepository, atLeastOnce()).findById(idPrenotazione);
        reset(prenotazioneRepository);
    }

    //TODO rivedere
    @Test
    void findAllPrenotazioniByIdHotel() {
        Long idHotel = 0L;
        Long idCliente = 0L;
        Long idCategoria = 0L;
        Long idStanza = 0L;



        List<Prenotazione> listPrenotazioni = List.of(/*prenotazione1, prenotazione2, prenotazione3*/);

        List<FatturaDTO> listFatture = prenotazioneHelper.convertPrenotazioneToFattura(listPrenotazioni);

        lenient().when(hotelRepository.existsById(idHotel)).thenReturn(true);
        lenient().when(prenotazioneRepository.findPrenotazionesByHotel_Id(idHotel)).thenReturn(listPrenotazioni);
        assertEquals(listFatture, prenotazioneHelper.findAllPrenotazioniByIdHotel(idHotel));
        verify(hotelRepository, atLeastOnce()).existsById(idHotel);
        verify(prenotazioneRepository, atLeastOnce()).findPrenotazionesByHotel_Id(idHotel);
        reset(hotelRepository);
        reset(prenotazioneRepository);
    }

    @Test
    void findAllFattureByIdCliente() {
    }

    @Test
    void deletePrenotazione() {
    }

    @Test
    void createPrenotazione() {
    }

    @Test
    void findPrenotazioniByStanzaId() {
    }

    @Test
    void updatePrenotazione() {
    }

    @Test
    void findAllByNomeCognomeClienteAndDataInizioAndDataFine() {
    }

    @Test
    void convertPrenotazioneToFattura() {
    }
}