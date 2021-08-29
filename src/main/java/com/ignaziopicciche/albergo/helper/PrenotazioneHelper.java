package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.repository.PrenotazioneRepository;
import com.ignaziopicciche.albergo.repository.StanzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrenotazioneHelper {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private StanzaRepository stanzaRepository;



}
