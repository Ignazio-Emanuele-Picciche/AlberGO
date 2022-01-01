package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.model.Amministratore;
import com.ignaziopicciche.albergo.repository.AmministratoreRepository;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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