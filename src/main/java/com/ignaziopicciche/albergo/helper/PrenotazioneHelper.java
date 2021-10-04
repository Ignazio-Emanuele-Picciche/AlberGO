package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.exception.ClienteException;
import com.ignaziopicciche.albergo.exception.HotelException;
import com.ignaziopicciche.albergo.exception.PrenotazioneException;
import com.ignaziopicciche.albergo.exception.StanzaException;
import com.ignaziopicciche.albergo.model.*;
import com.ignaziopicciche.albergo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrenotazioneHelper {

    private final PrenotazioneRepository prenotazioneRepository;
    private final HotelRepository hotelRepository;
    private final ClienteRepository clienteRepository;
    private final StanzaRepository stanzaRepository;
    private final CategoriaRepository categoriaRepository;

    public PrenotazioneHelper(PrenotazioneRepository prenotazioneRepository, HotelRepository hotelRepository, ClienteRepository clienteRepository, StanzaRepository stanzaRepository, CategoriaRepository categoriaRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.hotelRepository = hotelRepository;
        this.clienteRepository = clienteRepository;
        this.stanzaRepository = stanzaRepository;
        this.categoriaRepository = categoriaRepository;
    }


    public PrenotazioneDTO findById(Long id) {
        if (prenotazioneRepository.existsById(id)) {
            return new PrenotazioneDTO(prenotazioneRepository.findById(id).get());
        }

        throw new PrenotazioneException(PrenotazioneException.PrenotazioneExceptionCode.PRENOTAZIONE_ID_NOT_EXIST);
    }

    public List<FatturaDTO> findAll(Long idHotel) {
        if (hotelRepository.existsById(idHotel)) {
            List<Prenotazione> prenotazioni = prenotazioneRepository.findPrenotazionesByHotel_Id(idHotel);
            List<FatturaDTO> prenotazioniList = new ArrayList<>();

            for (Prenotazione p : prenotazioni) {
                Cliente cliente = p.getCliente();
                Stanza stanza = p.getStanza();
                Categoria categoria = p.getStanza().getCategoria();
                Hotel hotel = p.getHotel();


                prenotazioniList.add(new FatturaDTO(p, cliente, stanza, categoria, hotel));
            }

            return prenotazioniList;
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);
    }

    public List<FatturaDTO> findAllFatture(Long idCliente) {
        if (clienteRepository.existsById(idCliente)) {


            Cliente cliente = clienteRepository.findById(idCliente).get();
            List<Prenotazione> prenotazioni = prenotazioneRepository.findPrenotazionesByCliente_Id(idCliente);
            List<FatturaDTO> fattureList = new ArrayList<>();

            for (Prenotazione p : prenotazioni) {
                Hotel hotel = p.getHotel();
                Stanza stanza = p.getStanza();
                Categoria categoria = p.getStanza().getCategoria();

                fattureList.add(new FatturaDTO(p, cliente, stanza, categoria, hotel));
            }

            return fattureList;
        }

        throw new ClienteException(ClienteException.ClienteExcpetionCode.CLIENTE_ID_NOT_EXIST);
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


    public PrenotazioneDTO create(PrenotazioneDTO prenotazioneDTO) {
        if (prenotazioneRepository.checkPrenotazioneDate(prenotazioneDTO.dataInizio, prenotazioneDTO.dataFine) == 0 && prenotazioneDTO.dataInizio.before(prenotazioneDTO.dataFine)) {
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


    public List<PrenotazioneDTO> findPrenotazionesByStanza_Id(Long idStanza) {
        if (stanzaRepository.existsById(idStanza)) {
            List<Prenotazione> prenotazioniLista = prenotazioneRepository.findPrenotazionesByStanza_Id(idStanza);
            return prenotazioniLista.stream().map(x -> new PrenotazioneDTO(x)).collect(Collectors.toList());
        }

        throw new StanzaException(StanzaException.StanzaExceptionCode.STANZA_ID_NOT_EXIST);
    }


    public Long update(PrenotazioneDTO prenotazioneDTO){
        if(prenotazioneRepository.checkPrenotazioneDateUpdate(prenotazioneDTO.dataInizio, prenotazioneDTO.dataFine, prenotazioneDTO.id) == 0
                && prenotazioneDTO.dataInizio.before(prenotazioneDTO.dataFine) &&
                prenotazioneRepository.existsById(prenotazioneDTO.id)){

            Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneDTO.id).get();
            prenotazione.setDataInizio(prenotazioneDTO.dataInizio);
            prenotazione.setDataFine(prenotazioneDTO.dataFine);

            prenotazioneRepository.save(prenotazione);
            return prenotazione.getId();

        }
        throw new PrenotazioneException(PrenotazioneException.PrenotazioneExceptionCode.PRENOTAZIONE_DATE_NOT_COMPATIBLE);

    }

}
