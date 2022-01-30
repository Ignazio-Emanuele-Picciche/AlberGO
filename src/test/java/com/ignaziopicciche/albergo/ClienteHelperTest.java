package com.ignaziopicciche.albergo;

import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.helper.ClienteHelper;
import com.ignaziopicciche.albergo.helper.ClienteHotelHelper;
import com.ignaziopicciche.albergo.helper.StripeHelper;
import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.repository.AmministratoreRepository;
import com.ignaziopicciche.albergo.repository.ClienteHotelRepository;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteHelperTest {

    private ClienteHelper clienteHelper;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ClienteHotelRepository clienteHotelRepository;

    @Mock
    private AmministratoreRepository amministratoreRepository;

    @Mock
    private StripeHelper stripeHelper;

    @Mock
    private ClienteHotelHelper clienteHotelHelper;

    @Mock
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        clienteHelper = new ClienteHelper(clienteRepository, hotelRepository, stripeHelper, clienteHotelRepository, clienteHotelHelper, amministratoreRepository, passwordEncoder);
    }

    @Test
    void findClienteByIdTest(){
        Long clienteId = 0L;
        Cliente cliente = new Cliente();

        lenient().when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        lenient().when(clienteRepository.existsById(clienteId)).thenReturn(true);
        assertEquals(cliente, clienteHelper.findById(clienteId));
        verify(clienteRepository, atLeastOnce()).findById(clienteId);
        verify(clienteRepository, atLeastOnce()).existsById(clienteId);
        reset(clienteRepository);
    }

    @Test
    void findAllClientiByIdHotelTest(){
        Long idHotel = 0L;
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        Cliente cliente3 = new Cliente();
        List<Cliente> list = List.of(cliente1, cliente2, cliente3);

        lenient().when(hotelRepository.existsById(idHotel)).thenReturn(true);
        lenient().when(clienteRepository.findClientiByHotel_Id(idHotel)).thenReturn(list);
        assertEquals(list, clienteHelper.findAll(idHotel));
        verify(clienteRepository, atLeastOnce()).findClientiByHotel_Id(idHotel);
        verify(hotelRepository, atLeastOnce()).existsById(idHotel);
        reset(clienteRepository);
        reset(hotelRepository);
    }

    @Test
    void findClientiStartingWithNomeTest(){
        String nome = "ignazio";
        String cognome = null;
        Long idHotel = 0L;

        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        Cliente cliente3 = new Cliente();
        List<Cliente> list = List.of(cliente1, cliente2, cliente3);

        lenient().when(clienteRepository.findClientesByNomeStartingWith(nome, idHotel)).thenReturn(list);
        assertEquals(list, clienteHelper.findAllByNomeCognome(nome, cognome, idHotel));
        verify(clienteRepository, atLeastOnce()).findClientesByNomeStartingWith(nome, idHotel);
        reset(clienteRepository);
    }

    @Test
    void findClientiStartingWithCognomeTest(){
        String nome = null;
        String cognome = "picciche";
        Long idHotel = 0L;

        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();
        Cliente cliente3 = new Cliente();
        List<Cliente> list = List.of(cliente1, cliente2, cliente3);

        lenient().when(clienteRepository.findClientesByCognomeStartingWith(cognome, idHotel)).thenReturn(list);
        assertEquals(list, clienteHelper.findAllByNomeCognome(nome, cognome, idHotel));
        verify(clienteRepository, atLeastOnce()).findClientesByCognomeStartingWith(cognome, idHotel);
        reset(clienteRepository);
    }

    @Test
    void findClienteByUsername(){
        String username = "pippo";
        Cliente cliente = new Cliente();

        lenient().when(clienteRepository.existsByUsername(username)).thenReturn(true);
        lenient().when(clienteRepository.findByUsername(username)).thenReturn(cliente);
        assertEquals(cliente, clienteHelper.findClienteByUsername(username));
        verify(clienteRepository, atLeastOnce()).findByUsername(username);
        verify(clienteRepository, atLeastOnce()).existsByUsername(username);
        reset(clienteRepository);
    }

    @Test
    void createClienteTest() throws Exception {
        Long idCliente = 0L;
        String documento = "ASER";
        String usernmae = "username";
        ClienteDTO clienteDTO = ClienteDTO.builder()
                .id(idCliente)
                .documento(documento)
                .username(usernmae).build();

        lenient().when(clienteRepository.existsByDocumentoOrUsername(clienteDTO.documento, clienteDTO.username)).thenReturn(false);
        lenient().when(amministratoreRepository.existsByUsername(clienteDTO.username)).thenReturn(false);
        lenient().when(hotelRepository.findAll()).thenReturn(null);

        Assertions.assertNull(clienteHelper.createCliente(clienteDTO));

        verify(clienteRepository, atLeastOnce()).existsByDocumentoOrUsername(clienteDTO.documento, clienteDTO.username);
        verify(amministratoreRepository, atLeastOnce()).existsByUsername(clienteDTO.username);
        reset(clienteRepository);
        reset(amministratoreRepository);
    }

}