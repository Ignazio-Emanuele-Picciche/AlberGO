package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.PrenotazioneClienteStanzaCategoriaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.exception.HotelException;
import com.ignaziopicciche.albergo.exception.PrenotazioneException;
import com.ignaziopicciche.albergo.exception.StanzaException;
import com.ignaziopicciche.albergo.model.Categoria;
import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.model.Prenotazione;
import com.ignaziopicciche.albergo.model.Stanza;
import com.ignaziopicciche.albergo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private CategoriaRepository categoriaRepository;


    public PrenotazioneDTO findById(Long id) {
        if (prenotazioneRepository.existsById(id)) {
            return new PrenotazioneDTO(prenotazioneRepository.findById(id).get());
        }

        throw new PrenotazioneException(PrenotazioneException.PrenotazioneExceptionCode.PRENOTAZIONE_ID_NOT_EXIST);
    }

    public List<PrenotazioneClienteStanzaCategoriaDTO> findAll(Long idHotel) {
        if (hotelRepository.existsById(idHotel)) {
            List<Prenotazione> prenotazioni = prenotazioneRepository.findPrenotazionesByHotel_Id(idHotel);
            List<PrenotazioneClienteStanzaCategoriaDTO> prenotazioniList = new ArrayList<>();

            for (Prenotazione p : prenotazioni) {
                Cliente cli = clienteRepository.findById(p.getCliente().getId()).get();
                Stanza s = stanzaRepository.findById(p.getStanza().getId()).get();
                Categoria cat = categoriaRepository.findById(p.getStanza().getCategoria().getId()).get();

                prenotazioniList.add(new PrenotazioneClienteStanzaCategoriaDTO(p, cli, s, cat));
            }

            return prenotazioniList;
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);
    }


    public Boolean delete(Long id) {
        if (prenotazioneRepository.existsById(id)) {
            try {
                prenotazioneRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                throw new PrenotazioneException(PrenotazioneException.PrenotazioneExceptionCode.PRENOTAZIONE_DELETE_ERROR);
            }
        }

        throw new PrenotazioneException(PrenotazioneException.PrenotazioneExceptionCode.PRENOTAZIONE_NOT_FOUND);
    }


    public PrenotazioneDTO create (PrenotazioneDTO prenotazioneDTO){
        if(prenotazioneRepository.checkPrenotazioneDate(prenotazioneDTO.dataInizio, prenotazioneDTO.dataFine) == 0 && prenotazioneDTO.dataInizio.before(prenotazioneDTO.dataFine)){
            Prenotazione prenotazione = new Prenotazione();

            prenotazione.setDataInizio(prenotazioneDTO.dataInizio);
            prenotazione.setDataFine(prenotazioneDTO.dataFine);
            prenotazione.setHotel(hotelRepository.findById(prenotazioneDTO.idHotel).get());
            prenotazione.setStanza(stanzaRepository.findById(prenotazioneDTO.idStanza).get());
            prenotazione.setCliente(clienteRepository.findById(prenotazioneDTO.idCliente).get());

            prenotazioneRepository.save(prenotazione);

            return new PrenotazioneDTO(prenotazione);
        }

        throw new PrenotazioneException(PrenotazioneException.PrenotazioneExceptionCode.PRENOTAZIONE_DATE_NOT_COMPATIBLE);
    }


    public List<PrenotazioneDTO> findPrenotazionesByStanza_Id(Long idStanza){
        if(stanzaRepository.existsById(idStanza)){
            List<Prenotazione> prenotazioniLista = prenotazioneRepository.findPrenotazionesByStanza_Id(idStanza);
            return prenotazioniLista.stream().map(x -> new PrenotazioneDTO(x)).collect(Collectors.toList());
        }

        throw new StanzaException(StanzaException.StanzaExceptionCode.STANZA_ID_NOT_EXIST);
    }

}
