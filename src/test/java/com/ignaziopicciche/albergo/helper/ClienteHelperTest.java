package com.ignaziopicciche.albergo.helper;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.AmministratoreRepository;
import com.ignaziopicciche.albergo.repository.ClienteHotelRepository;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void findClienteById() {
        /*
        AppUser user1 = new AppUser();
		AppUser user2 = new AppUser();
		AppUser user3 = new AppUser();
		List<AppUser> list = List.of(user1, user2, user3);

		lenient().when(userRepository.findAll()).thenReturn(list);
		assertEquals(list, service.getUsers());
		verify(userRepository, atLeastOnce()).findAll();
		reset(userRepository);
         */

//        Long idCliente = clienteRepository.findAll().get(0).getId();
//        Cliente cliente = clienteRepository.findById(idCliente).get();
        Long idCliente = 3L;

        Cliente cliente = new Cliente();
        lenient().when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        assertEquals(cliente, clienteHelper.findById(idCliente));
        verify(clienteRepository, atLeastOnce()).findById(idCliente);
        reset(clienteRepository);
    }

}