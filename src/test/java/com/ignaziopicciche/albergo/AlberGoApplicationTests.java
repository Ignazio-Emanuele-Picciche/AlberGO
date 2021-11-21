package com.ignaziopicciche.albergo;

import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.service.*;
import com.stripe.exception.StripeException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
class AlberGoApplicationTests {

    private final PrenotazioneService prenotazioneService;
    private final ClienteService clienteService;
    private final HotelService hotelService;
    private final StanzaService stanzaService;
    private final CategoriaService categoriaService;
    private final ServizioService servizioService;
    private final StripeService stripeService;

    private final ClienteRepository clienteRepository;

    AlberGoApplicationTests(PrenotazioneService prenotazioneService, ClienteService clienteService, HotelService hotelService, StanzaService stanzaService, CategoriaService categoriaService, ServizioService servizioService, StripeService stripeService, ClienteRepository clienteRepository) {
        this.prenotazioneService = prenotazioneService;
        this.clienteService = clienteService;
        this.hotelService = hotelService;
        this.stanzaService = stanzaService;
        this.categoriaService = categoriaService;
        this.servizioService = servizioService;
        this.stripeService = stripeService;
        this.clienteRepository = clienteRepository;
    }

    private


    @Test
    void contextLoads() {
    }


    //------------------------- PRENOTAZIONE -------------------------//

    @Test
    private void createPrenotazione() throws StripeException, ParseException {
        Date dataInizio = new SimpleDateFormat("yyyy-MM-dd").parse("2022-03-05");
        Date dataFine =  new SimpleDateFormat("yyyy-MM-dd").parse("2022-03-15");

        PrenotazioneDTO prenotazioneDTO = PrenotazioneDTO.builder()
                .dataInizio(dataInizio)
                .dataFine(dataFine)
                /*.idHotel()
                .idCliente()
                .idStanza()*/.build();

        PrenotazioneDTO response = prenotazioneService.create(prenotazioneDTO);

    }


    //------------------------- CLIENTE -------------------------//

    @Test
    public void createCliente() throws Exception {
        ClienteDTO clienteDTO = ClienteDTO.builder()
                .nome("Ignazio")
                .cognome("Picciche")
                .documento("ddssaa")
                .telefono("3290468479")
                .username("ciao")
                .password("ciao").build();

        Long idCliente = clienteService.create(clienteDTO);

        Cliente cliente = clienteRepository.findById(idCliente).get();
    }

}
