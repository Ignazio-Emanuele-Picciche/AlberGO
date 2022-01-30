package com.ignaziopicciche.albergo;

import com.ignaziopicciche.albergo.dto.StanzaDTO;
import com.ignaziopicciche.albergo.helper.StanzaHelper;
import com.ignaziopicciche.albergo.model.Categoria;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.model.Stanza;
import com.ignaziopicciche.albergo.repository.CategoriaRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.repository.StanzaRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StanzaHelperTest {

    StanzaHelper stanzaHelper;

    @Mock
    private StanzaRepository stanzaRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @BeforeEach
    void setUp() {
        stanzaHelper = new StanzaHelper(stanzaRepository, hotelRepository, categoriaRepository);
    }

    @Test
    void deleteStanzaTest() {
        Long idStanza = 0L;

        lenient().when(stanzaRepository.existsById(idStanza)).thenReturn(true);
        Assertions.assertTrue(stanzaHelper.delete(idStanza));
        verify(stanzaRepository, atLeastOnce()).existsById(idStanza);
        reset(stanzaRepository);
    }

    @Test
    void updateStanzaTest() {
        Long idStanza = 0L;
        String campoUpdate = "test";

        Stanza updateStanza = Stanza.builder()
                .id(idStanza).build();
        StanzaDTO stanzaDTO = StanzaDTO.builder()
                .id(idStanza)
                .descrizione(campoUpdate).build();


        lenient().when(stanzaRepository.existsById(idStanza)).thenReturn(true);
        lenient().when(stanzaRepository.findById(idStanza)).thenReturn(Optional.ofNullable(updateStanza));
        assert updateStanza != null;
        lenient().when(stanzaRepository.save(updateStanza)).thenReturn(updateStanza);
        assertEquals(updateStanza, stanzaHelper.updateStanza(stanzaDTO));
        assertEquals(campoUpdate, updateStanza.getDescrizione());
        verify(stanzaRepository, atLeastOnce()).existsById(idStanza);
        verify(stanzaRepository, atLeastOnce()).findById(idStanza);
        verify(stanzaRepository, atLeastOnce()).save(updateStanza);
        reset(stanzaRepository);
    }

    @Test
    void findStanzaByIdTest() {
        Long idStanza = 0L;
        Stanza stanza = Stanza.builder()
                .id(idStanza).build();

        lenient().when(stanzaRepository.existsById(idStanza)).thenReturn(true);
        lenient().when(stanzaRepository.findById(idStanza)).thenReturn(Optional.ofNullable(stanza));
        assertEquals(stanza, stanzaHelper.findById(idStanza));
        verify(stanzaRepository, atLeastOnce()).existsById(idStanza);
        verify(stanzaRepository, atLeastOnce()).findById(idStanza);
        reset(stanzaRepository);
    }

    @Test
    void findAllStanzeByIdHotelTest() {
        Long idHotel = 0L;
        Stanza stanza1 = new Stanza();
        Stanza stanza2 = new Stanza();
        Stanza stanza3 = new Stanza();
        List<Stanza> list = List.of(stanza1, stanza2, stanza3);

        lenient().when(hotelRepository.existsById(idHotel)).thenReturn(true);
        lenient().when(stanzaRepository.findStanzasByHotel_Id(idHotel)).thenReturn(list);
        assertEquals(list, stanzaHelper.findAllStanzeByIdHotel(idHotel));
        verify(hotelRepository, atLeastOnce()).existsById(idHotel);
        verify(stanzaRepository, atLeastOnce()).findStanzasByHotel_Id(idHotel);
        reset(stanzaRepository);
        reset(hotelRepository);
    }

    @Test
    void findStanzeByCategoriaIdAndDatesTest() throws ParseException {
        Long idCategoria = 0L;
        String dataInizioStringa = "2020-05-10";
        String dataFineStringa = "2020-05-20";

        Date dataInizio = StringUtils.isNotBlank(dataInizioStringa) ? new SimpleDateFormat("yyyy-MM-dd").parse(dataInizioStringa) : null;
        Date dataFine = StringUtils.isNotBlank(dataFineStringa) ? new SimpleDateFormat("yyyy-MM-dd").parse(dataFineStringa) : null;

        Stanza stanza1 = new Stanza();
        Stanza stanza2 = new Stanza();
        Stanza stanza3 = new Stanza();
        List<Stanza> list = List.of(stanza1, stanza2, stanza3);

        lenient().when(categoriaRepository.existsById(idCategoria)).thenReturn(true);
        lenient().when(stanzaRepository.findStanzasByCategoria_IdAndDates(idCategoria, dataInizio, dataFine)).thenReturn(list);
        assertEquals(list, stanzaHelper.findStanzasByCategoria_IdAndDates(idCategoria, dataInizio, dataFine));
        verify(categoriaRepository, atLeastOnce()).existsById(idCategoria);
        verify(stanzaRepository, atLeastOnce()).findStanzasByCategoria_IdAndDates(idCategoria, dataInizio, dataFine);
        reset(categoriaRepository);
        reset(stanzaRepository);
    }

    @Test
    void findNumeroStanzeFuoriServizioByIdHotelTest(){
        Long idHotel = 0L;
        int numeroStanzeFuoriServizio = 5;

        lenient().when(hotelRepository.existsById(idHotel)).thenReturn(true);
        lenient().when(stanzaRepository.findCountStanzasFuoriServizioByHotel_Id(idHotel)).thenReturn(numeroStanzeFuoriServizio);
        assertEquals(numeroStanzeFuoriServizio, stanzaHelper.findNumeroStanzeFuoriServizioByIdHotel(idHotel));
        verify(hotelRepository, atLeastOnce()).existsById(idHotel);
        verify(stanzaRepository, atLeastOnce()).findCountStanzasFuoriServizioByHotel_Id(idHotel);
        reset(hotelRepository);
        reset(stanzaRepository);
    }

    @Test
    void findStanzeLibereByHotelIdAndDatesTest() throws ParseException {
        Long idHotel = 0L;
        String dataInizioStringa = "2020-05-10";
        String dataFineStringa = "2020-05-20";
        Date dataInizio = StringUtils.isNotBlank(dataInizioStringa) ? new SimpleDateFormat("yyyy-MM-dd").parse(dataInizioStringa) : null;
        Date dataFine = StringUtils.isNotBlank(dataFineStringa) ? new SimpleDateFormat("yyyy-MM-dd").parse(dataFineStringa) : null;

        Stanza stanza1 = new Stanza();
        Stanza stanza2 = new Stanza();
        Stanza stanza3 = new Stanza();
        List<Stanza> list = List.of(stanza1, stanza2, stanza3);

        lenient().when(hotelRepository.existsById(idHotel)).thenReturn(true);
        lenient().when(stanzaRepository.findStanzasLibereByHotel_IdAndDates(idHotel, dataInizio, dataFine)).thenReturn(list);
        assertEquals(list, stanzaHelper.findStanzeLibereByHotel_IdAndDates(idHotel, dataInizio, dataFine));
        verify(hotelRepository, atLeastOnce()).existsById(idHotel);
        verify(stanzaRepository, atLeastOnce()).findStanzasLibereByHotel_IdAndDates(idHotel, dataInizio, dataFine);
        reset(hotelRepository);
        reset(stanzaRepository);
    }

    @Test
    void findStanzeOccupateByHotelIdAndDatesTest() throws ParseException {
        Long idHotel = 0L;
        String dataInizioStringa = "2020-05-10";
        String dataFineStringa = "2020-05-20";
        Date dataInizio = StringUtils.isNotBlank(dataInizioStringa) ? new SimpleDateFormat("yyyy-MM-dd").parse(dataInizioStringa) : null;
        Date dataFine = StringUtils.isNotBlank(dataFineStringa) ? new SimpleDateFormat("yyyy-MM-dd").parse(dataFineStringa) : null;

        Stanza stanza1 = new Stanza();
        Stanza stanza2 = new Stanza();
        Stanza stanza3 = new Stanza();
        List<Stanza> list = List.of(stanza1, stanza2, stanza3);

        lenient().when(hotelRepository.existsById(idHotel)).thenReturn(true);
        lenient().when(stanzaRepository.findStanzasOccupateByHotel_IdAndDates(idHotel, dataInizio, dataFine)).thenReturn(list);
        assertEquals(list, stanzaHelper.findStanzeOccupateByHotel_IdAndDates(idHotel, dataInizio, dataFine));
        verify(hotelRepository, atLeastOnce()).existsById(idHotel);
        verify(stanzaRepository, atLeastOnce()).findStanzasOccupateByHotel_IdAndDates(idHotel, dataInizio, dataFine);
        reset(hotelRepository);
        reset(stanzaRepository);
    }

    @Test
    void findCountStanzeByCategoria_IdTest(){
        Long idCategoria = 0L;
        int numeroStanze = 5;

        lenient().when(categoriaRepository.existsById(idCategoria)).thenReturn(true);
        lenient().when(stanzaRepository.findCountStanzeByCategoria_Id(idCategoria)).thenReturn(numeroStanze);
        assertEquals(numeroStanze, stanzaHelper.findCountStanzeByCategoria_Id(idCategoria));
        verify(categoriaRepository, atLeastOnce()).existsById(idCategoria);
        verify(stanzaRepository, atLeastOnce()).findCountStanzeByCategoria_Id(idCategoria);
        reset(categoriaRepository);
        reset(stanzaRepository);
    }

    @Test
    void createStanzaTest(){
        Integer numeroStanza = 1;
        String descrizioneStanza = "descrizioneTest";
        Long idHotel = 0L;
        Long idCategoria = 0L;
        Long idStanza = 0L;
        Hotel hotel = Hotel.builder().id(idHotel).build();
        Categoria categoria = Categoria.builder().id(idCategoria).build();
        StanzaDTO stanzaDTO = StanzaDTO.builder()
                .id(idStanza)
                .numeroStanza(numeroStanza)
                .descrizione(descrizioneStanza)
                .idHotel(idHotel).build();

        lenient().when(stanzaRepository.existsStanzaByNumeroStanzaAndHotel_Id(stanzaDTO.numeroStanza, stanzaDTO.idHotel)).thenReturn(false);
        lenient().when(hotelRepository.findById(stanzaDTO.idHotel)).thenReturn(Optional.ofNullable(hotel));
        lenient().when(categoriaRepository.findById(stanzaDTO.idCategoria)).thenReturn(Optional.ofNullable(categoria));
        Assertions.assertNull(stanzaHelper.createStanza(stanzaDTO));
        verify(stanzaRepository, atLeastOnce()).existsStanzaByNumeroStanzaAndHotel_Id(stanzaDTO.numeroStanza, stanzaDTO.idHotel);
        verify(hotelRepository, atLeastOnce()).findById(stanzaDTO.idHotel);
        verify(categoriaRepository, atLeastOnce()).findById(stanzaDTO.idCategoria);
        reset(stanzaRepository);
        reset(hotelRepository);
        reset(categoriaRepository);
    }

}