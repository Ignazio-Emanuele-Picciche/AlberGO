package com.ignaziopicciche.albergo;

import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import com.ignaziopicciche.albergo.helper.CategoriaHelper;
import com.ignaziopicciche.albergo.model.Categoria;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.CategoriaRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaHelperTest {

    CategoriaHelper categoriaHelper;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        categoriaHelper = new CategoriaHelper(categoriaRepository, hotelRepository);
    }

    @Test
    void updateCategoriaTest() {
        Long idCategoria = 0L;
        String campoUpdate = "prova";

        Categoria updateCategoria = Categoria.builder()
                .id(idCategoria).build();

        CategoriaDTO categoriaDTO = CategoriaDTO.builder()
                .id(idCategoria)
                .descrizione(campoUpdate).build();

        lenient().when(categoriaRepository.existsById(idCategoria)).thenReturn(true);
        lenient().when(categoriaRepository.findById(idCategoria)).thenReturn(Optional.ofNullable(updateCategoria));
        assert updateCategoria != null;
        lenient().when(categoriaRepository.save(updateCategoria)).thenReturn(updateCategoria);
        assertEquals(idCategoria, categoriaHelper.update(categoriaDTO));
        assertEquals(campoUpdate, updateCategoria.getDescrizione());
        verify(categoriaRepository, atLeastOnce()).existsById(idCategoria);
        verify(categoriaRepository, atLeastOnce()).findById(idCategoria);
        verify(categoriaRepository, atLeastOnce()).save(updateCategoria);
        reset(categoriaRepository);
    }

    @Test
    void deleteCategoriaTest() {
        Long idCategoria = 0L;

        lenient().when(categoriaRepository.existsById(idCategoria)).thenReturn(true);
        Assertions.assertTrue(categoriaHelper.delete(idCategoria));
        verify(categoriaRepository, atLeastOnce()).existsById(idCategoria);
        reset(categoriaRepository);
    }

    @Test
    void findCategoriaByIdTest(){
        Long idCategoria = 0L;
        Categoria categoria = new Categoria();

        lenient().when(categoriaRepository.existsById(idCategoria)).thenReturn(true);
        lenient().when(categoriaRepository.findById(idCategoria)).thenReturn(Optional.of(categoria));
        assertEquals(categoria, categoriaHelper.findById(idCategoria));
        verify(categoriaRepository, atLeastOnce()).existsById(idCategoria);
        verify(categoriaRepository, atLeastOnce()).findById(idCategoria);
        reset(categoriaRepository);
    }

    @Test
    void findAllCategoriaByIdHotelTest(){
        Long idHotel = 0L;

        Categoria categoria1 = new Categoria();
        Categoria categoria2 = new Categoria();
        Categoria categoria3 = new Categoria();
        List<Categoria> list = List.of(categoria1, categoria2, categoria3);

        lenient().when(hotelRepository.existsById(idHotel)).thenReturn(true);
        lenient().when(categoriaRepository.findCategoriasByHotel_Id(idHotel)).thenReturn(list);
        assertEquals(list, categoriaHelper.findAll(idHotel));
        verify(hotelRepository, atLeastOnce()).existsById(idHotel);
        verify(categoriaRepository, atLeastOnce()).findCategoriasByHotel_Id(idHotel);
        reset(categoriaRepository);
    }

    @Test
    void findAllCategorieStartingWithNomeTest(){
        String nome = "nomeCategoria";
        Long idHotel = 0L;

        Categoria categoria1 = new Categoria();
        Categoria categoria2 = new Categoria();
        Categoria categoria3 = new Categoria();
        List<Categoria> list = List.of(categoria1, categoria2, categoria3);

        lenient().when(categoriaRepository.findCategoriasByNomeStartingWithAndHotel_Id(nome, idHotel)).thenReturn(list);
        assertEquals(list, categoriaHelper.findAllByNome(nome, idHotel));
        verify(categoriaRepository, atLeastOnce()).findCategoriasByNomeStartingWithAndHotel_Id(nome, idHotel);
        reset(categoriaRepository);
    }

    @Test
    void createCategoriaTest(){
        Long idHotel = 0L;
        Long idCategoria = 0L;
        String nomeCategoria = "nomeCategoria";
        String descrizione = "descrizioneCategoria";
        CategoriaDTO categoriaDTO = CategoriaDTO.builder()
                .id(idCategoria)
                .nome(nomeCategoria)
                .descrizione(descrizione)
                .idHotel(idHotel).build();
        Hotel hotel = Hotel.builder().id(idHotel).build();

        lenient().when(categoriaRepository.existsCategoriaByNomeAndHotel_Id(nomeCategoria, idHotel)).thenReturn(false);
        lenient().when(hotelRepository.findById(idHotel)).thenReturn(Optional.ofNullable(hotel));
        Assertions.assertNull(categoriaHelper.create(categoriaDTO));
        verify(categoriaRepository, atLeastOnce()).existsCategoriaByNomeAndHotel_Id(nomeCategoria, idHotel);
        verify(hotelRepository, atLeastOnce()).findById(idHotel);
        reset(categoriaRepository);
        reset(hotelRepository);
    }
}