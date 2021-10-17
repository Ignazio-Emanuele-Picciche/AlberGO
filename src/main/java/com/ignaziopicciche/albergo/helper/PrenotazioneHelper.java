package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.enums.HotelEnum;
import com.ignaziopicciche.albergo.enums.PrenotazioneEnum;
import com.ignaziopicciche.albergo.enums.StanzaEnum;
import com.ignaziopicciche.albergo.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.*;
import com.ignaziopicciche.albergo.repository.*;
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

    private static PrenotazioneEnum prenotazioneEnum;
    private static HotelEnum hotelEnum;
    private static StanzaEnum stanzaEnum;

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

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_IDNE");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
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

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
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

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_IDNE");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }


    public Boolean delete(Long id) {
        if (prenotazioneRepository.existsById(id)) {
            try {
                prenotazioneRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DLE");
                throw new ApiRequestException(prenotazioneEnum.getMessage());
            }
        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_NF");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }


    public PrenotazioneDTO create(PrenotazioneDTO prenotazioneDTO) {
        if (prenotazioneRepository.checkPrenotazioneDate(prenotazioneDTO.dataInizio, prenotazioneDTO.dataFine, prenotazioneDTO.idStanza) == 0 && prenotazioneDTO.dataInizio.before(prenotazioneDTO.dataFine)) {
            Prenotazione prenotazione = new Prenotazione();

            prenotazione.setDataInizio(prenotazioneDTO.dataInizio);
            prenotazione.setDataFine(prenotazioneDTO.dataFine);
            prenotazione.setHotel(hotelRepository.findById(prenotazioneDTO.idHotel).get());
            prenotazione.setStanza(stanzaRepository.findById(prenotazioneDTO.idStanza).get());
            prenotazione.setCliente(clienteRepository.findById(prenotazioneDTO.idCliente).get());

            prenotazioneRepository.save(prenotazione);

            return new PrenotazioneDTO(prenotazione);
        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DNC");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }


    public List<PrenotazioneDTO> findPrenotazionesByStanza_Id(Long idStanza) {
        if (stanzaRepository.existsById(idStanza)) {
            List<Prenotazione> prenotazioniLista = prenotazioneRepository.findPrenotazionesByStanza_Id(idStanza);
            return prenotazioniLista.stream().map(x -> new PrenotazioneDTO(x)).collect(Collectors.toList());
        }

        stanzaEnum = StanzaEnum.getStanzaEnumByMessageCode("STA_IDNE");
        throw new ApiRequestException(stanzaEnum.getMessage());
    }


    public Long update(PrenotazioneDTO prenotazioneDTO) {
        if (prenotazioneRepository.checkPrenotazioneDateUpdate(prenotazioneDTO.dataInizio, prenotazioneDTO.dataFine, prenotazioneDTO.id, prenotazioneDTO.idStanza) == 0
                && prenotazioneDTO.dataInizio.before(prenotazioneDTO.dataFine) &&
                prenotazioneRepository.existsById(prenotazioneDTO.id)) {

            Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneDTO.id).get();
            prenotazione.setDataInizio(prenotazioneDTO.dataInizio);
            prenotazione.setDataFine(prenotazioneDTO.dataFine);

            prenotazioneRepository.save(prenotazione);
            return prenotazione.getId();

        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DNC");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }

}
