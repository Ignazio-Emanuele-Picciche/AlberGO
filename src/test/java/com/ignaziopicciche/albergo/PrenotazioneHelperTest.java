package com.ignaziopicciche.albergo;

import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.helper.PrenotazioneHelper;
import com.ignaziopicciche.albergo.helper.StripeHelper;
import com.ignaziopicciche.albergo.model.*;
import com.ignaziopicciche.albergo.repository.*;
import com.stripe.exception.StripeException;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

    @Test
    void findAllPrenotazioniByIdHotel() {
        Long idHotel = 0L;
        Long idCliente = 0L;
        Long idCategoria = 0L;
        Long idStanza = 0L;
        Long idPrenotazione1 = 0L;
        Long idPrenotazione2 = 1L;
        Long idPrenotazione3 = 2L;

        Hotel hotel = Hotel.builder().id(idHotel).build();

        Cliente cliente = Cliente.builder()
                .id(idCliente)
                .hotel(hotel).build();

        Categoria categoria = Categoria.builder()
                .id(idCategoria)
                .hotel(hotel).build();

        Stanza stanza = Stanza.builder()
                .id(idStanza)
                .categoria(categoria)
                .hotel(hotel).build();

        Prenotazione prenotazione1 = Prenotazione.builder()
                .id(idPrenotazione1)
                .hotel(hotel)
                .stanza(stanza)
                .cliente(cliente).build();

        Prenotazione prenotazione2 = Prenotazione.builder()
                .id(idPrenotazione2)
                .hotel(hotel)
                .stanza(stanza)
                .cliente(cliente).build();

        Prenotazione prenotazione3 = Prenotazione.builder()
                .id(idPrenotazione3)
                .hotel(hotel)
                .stanza(stanza)
                .cliente(cliente).build();

        List<Prenotazione> listPrenotazioni = List.of(prenotazione1, prenotazione2, prenotazione3);

        List<FatturaDTO> listFatture = prenotazioneHelper.convertPrenotazioneToFattura(listPrenotazioni);

        lenient().when(hotelRepository.existsById(idHotel)).thenReturn(true);
        lenient().when(prenotazioneRepository.findPrenotazionesByHotel_Id(idHotel)).thenReturn(listPrenotazioni);
        Assertions.assertNotNull(prenotazioneHelper.findAllPrenotazioniByIdHotel(idHotel));
        assertEquals(listFatture.get(0).prenotazione.id, prenotazioneHelper.findAllPrenotazioniByIdHotel(idHotel).get(0).prenotazione.id);
        verify(hotelRepository, atLeastOnce()).existsById(idHotel);
        verify(prenotazioneRepository, atLeastOnce()).findPrenotazionesByHotel_Id(idHotel);
        reset(hotelRepository);
        reset(prenotazioneRepository);
    }

    @Test
    void findAllFattureByIdCliente() {
        Long idHotel = 0L;
        Long idCliente = 0L;
        Long idCategoria = 0L;
        Long idStanza = 0L;
        Long idPrenotazione1 = 0L;
        Long idPrenotazione2 = 1L;
        Long idPrenotazione3 = 2L;

        Hotel hotel = Hotel.builder().id(idHotel).build();

        Cliente cliente = Cliente.builder()
                .id(idCliente)
                .hotel(hotel).build();

        Categoria categoria = Categoria.builder()
                .id(idCategoria)
                .hotel(hotel).build();

        Stanza stanza = Stanza.builder()
                .id(idStanza)
                .categoria(categoria)
                .hotel(hotel).build();

        Prenotazione prenotazione1 = Prenotazione.builder()
                .id(idPrenotazione1)
                .hotel(hotel)
                .stanza(stanza)
                .cliente(cliente).build();

        Prenotazione prenotazione2 = Prenotazione.builder()
                .id(idPrenotazione2)
                .hotel(hotel)
                .stanza(stanza)
                .cliente(cliente).build();

        Prenotazione prenotazione3 = Prenotazione.builder()
                .id(idPrenotazione3)
                .hotel(hotel)
                .stanza(stanza)
                .cliente(cliente).build();

        List<Prenotazione> listPrenotazioni = List.of(prenotazione1, prenotazione2, prenotazione3);

        List<FatturaDTO> listFatture = prenotazioneHelper.convertPrenotazioneToFattura(listPrenotazioni);

        lenient().when(clienteRepository.existsById(idCliente)).thenReturn(true);
        lenient().when(clienteRepository.findById(idCliente)).thenReturn(Optional.ofNullable(cliente));
        lenient().when(prenotazioneRepository.findPrenotazionesByCliente_Id(idCliente)).thenReturn(listPrenotazioni);
        Assertions.assertNotNull(prenotazioneHelper.findAllFattureByIdCliente(idCliente));
        assertEquals(listFatture.get(0).prenotazione.id, prenotazioneHelper.findAllFattureByIdCliente(idCliente).get(0).prenotazione.id);
        verify(clienteRepository, atLeastOnce()).existsById(idCliente);
        verify(clienteRepository, atLeastOnce()).findById(idCliente);
        verify(prenotazioneRepository, atLeastOnce()).findPrenotazionesByCliente_Id(idCliente);
        reset(clienteRepository);
        reset(prenotazioneRepository);
    }


    @Test
    void findPrenotazioniByStanzaId() {
        Long idStanza = 0L;
        Prenotazione prenotazione1 = new Prenotazione();
        Prenotazione prenotazione2 = new Prenotazione();
        Prenotazione prenotazione3 = new Prenotazione();
        List<Prenotazione> list = List.of(prenotazione1, prenotazione2, prenotazione3);

        lenient().when(stanzaRepository.existsById(idStanza)).thenReturn(true);
        lenient().when(prenotazioneRepository.findPrenotazionesByStanza_Id(idStanza)).thenReturn(list);
        assertEquals(list, prenotazioneHelper.findPrenotazioniByStanzaId(idStanza));
        verify(stanzaRepository, atLeastOnce()).existsById(idStanza);
        verify(prenotazioneRepository, atLeastOnce()).findPrenotazionesByStanza_Id(idStanza);
        reset(stanzaRepository);
        reset(prenotazioneRepository);
    }

    @Test
    void findAllByNomeCognomeClienteAndDataInizioAndDataFine() {
        //String nomeCliente, String cognomeCliente, Date dataInizio, Date dataFine, Long idHotel
        String nomeCliente = "test";
        String cognomeCliente = null;
        Date dataInizio = null;
        Date dataFine = null;

        Long idHotel = 0L;
        Long idCliente = 0L;
        Long idCategoria = 0L;
        Long idStanza = 0L;
        Long idPrenotazione1 = 0L;
        Long idPrenotazione2 = 1L;
        Long idPrenotazione3 = 2L;

        Hotel hotel = Hotel.builder().id(idHotel).build();

        Cliente cliente = Cliente.builder()
                .id(idCliente)
                .hotel(hotel).build();

        Categoria categoria = Categoria.builder()
                .id(idCategoria)
                .hotel(hotel).build();

        Stanza stanza = Stanza.builder()
                .id(idStanza)
                .categoria(categoria)
                .hotel(hotel).build();

        Prenotazione prenotazione1 = Prenotazione.builder()
                .id(idPrenotazione1)
                .hotel(hotel)
                .stanza(stanza)
                .cliente(cliente).build();

        Prenotazione prenotazione2 = Prenotazione.builder()
                .id(idPrenotazione2)
                .hotel(hotel)
                .stanza(stanza)
                .cliente(cliente).build();

        Prenotazione prenotazione3 = Prenotazione.builder()
                .id(idPrenotazione3)
                .hotel(hotel)
                .stanza(stanza)
                .cliente(cliente).build();

        List<Prenotazione> listPrenotazioni = List.of(prenotazione1, prenotazione2, prenotazione3);
        List<FatturaDTO> listFatture = prenotazioneHelper.convertPrenotazioneToFattura(listPrenotazioni);

        lenient().when(hotelRepository.existsById(idHotel)).thenReturn(true);
        lenient().when(prenotazioneRepository.findPrenotazionesByCliente_NomeStartingWithAndHotel_Id(nomeCliente, idHotel)).thenReturn(listPrenotazioni);
        assertEquals(listFatture.get(0).prenotazione.id, prenotazioneHelper.findAllByNomeCognomeClienteAndDataInizioAndDataFine(nomeCliente, cognomeCliente, dataInizio, dataFine, idHotel).get(0).prenotazione.id);
        verify(hotelRepository, atLeastOnce()).existsById(idHotel);
        verify(prenotazioneRepository, atLeastOnce()).findPrenotazionesByCliente_NomeStartingWithAndHotel_Id(nomeCliente, idHotel);

        nomeCliente = null;
        cognomeCliente = "testCognome";
        lenient().when(prenotazioneRepository.findPrenotazionesByCliente_CognomeStartingWithAndHotel_Id(cognomeCliente, idHotel)).thenReturn(listPrenotazioni);
        assertEquals(listFatture.get(0).prenotazione.id, prenotazioneHelper.findAllByNomeCognomeClienteAndDataInizioAndDataFine(nomeCliente, cognomeCliente, dataInizio, dataFine, idHotel).get(0).prenotazione.id);
        verify(prenotazioneRepository, atLeastOnce()).findPrenotazionesByCliente_CognomeStartingWithAndHotel_Id(cognomeCliente, idHotel);

        reset(hotelRepository);
        reset(prenotazioneRepository);
    }

    /*@Test
    void deletePrenotazione() throws StripeException, ParseException {
        Long idHotel = 0L;
        Long idCliente = 0L;
        Long idCategoria = 0L;
        Long idStanza = 0L;
        Long idPrenotazione1 = 0L;

        Hotel hotel = Hotel.builder().id(idHotel).build();

        Cliente cliente = Cliente.builder()
                .id(idCliente)
                .hotel(hotel).build();

        Categoria categoria = Categoria.builder()
                .id(idCategoria)
                .giorniPenale(10)
                .giorniBlocco(2)
                .hotel(hotel).build();

        Stanza stanza = Stanza.builder()
                .id(idStanza)
                .categoria(categoria)
                .hotel(hotel).build();

        Date dataInizio = new SimpleDateFormat("yyyy-MM-dd").parse("2022-05-06");
        Date dataFine = new SimpleDateFormat("yyyy-MM-dd").parse("2022-05-15");

        Prenotazione prenotazione1 = Prenotazione.builder()
                .id(idPrenotazione1)
                .hotel(hotel)
                .stanza(stanza)
                .cliente(cliente)
                .dataInizio(dataInizio)
                .dataFine(dataFine).build();

        lenient().when(prenotazioneRepository.existsById(idPrenotazione1)).thenReturn(true);
        lenient().when(prenotazioneRepository.getById(idPrenotazione1)).thenReturn(prenotazione1);
        Assertions.assertTrue(prenotazioneHelper.deletePrenotazione(idPrenotazione1));
        verify(prenotazioneRepository, atLeastOnce()).existsById(idPrenotazione1);
        verify(prenotazioneRepository, atLeastOnce()).getById(idPrenotazione1);
        reset(prenotazioneRepository);
    }*/
}