package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.AmministratoreDTO;
import com.ignaziopicciche.albergo.model.Amministratore;
import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.AmministratoreRepository;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmministratoreHelperTest {

    private AmministratoreHelper amministratoreHelper;

    @Mock
    private AmministratoreRepository amministratoreRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        amministratoreHelper = new AmministratoreHelper(amministratoreRepository, hotelRepository, clienteRepository, passwordEncoder);
    }


    @Test
    void createAmministratoreTest() {
        Long idAmministratore = 0L;
        Long idCliente = 0L;
        Long idHotel = 0L;
        String usernameAmministratore = "usernameAmministratore";
        String usernameCliente = "usernameCliente";
        Hotel hotel = Hotel.builder().id(idHotel).build();
        Amministratore amministratore = Amministratore.builder().id(idAmministratore).hotel(hotel).build();
        AmministratoreDTO amministratoreDTO = AmministratoreDTO.builder().id(idAmministratore).idHotel(idHotel).build();
        Cliente cliente = Cliente.builder().id(idCliente).build();

        lenient().when(amministratoreRepository.existsByUsername(usernameAmministratore)).thenReturn(false);
        lenient().when(clienteRepository.existsByUsername(usernameCliente)).thenReturn(false);
        lenient().when(hotelRepository.findById(idHotel)).thenReturn(Optional.ofNullable(hotel));

        Assertions.assertNull(amministratoreHelper.createAmministratore(amministratoreDTO));

        reset(clienteRepository);
        reset(amministratoreRepository);
        reset(hotelRepository);
    }

    @Test
    void findAmministratoreByUsernameTest() {
        String username = "test";
        Amministratore amministratore = Amministratore.builder()
                .username(username).build();

        lenient().when(amministratoreRepository.existsByUsername(username)).thenReturn(true);
        lenient().when(amministratoreRepository.findByUsername(username)).thenReturn(amministratore);
        assertEquals(amministratore, amministratoreHelper.findAmministratoreByUsername(username));
        verify(amministratoreRepository, atLeastOnce()).existsByUsername(username);
        verify(amministratoreRepository, atLeastOnce()).findByUsername(username);
        reset(amministratoreRepository);
    }

}