package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.AmministratoreDTO;
import com.ignaziopicciche.albergo.exception.enums.AmministratoreEnum;
import com.ignaziopicciche.albergo.exception.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.Amministratore;
import com.ignaziopicciche.albergo.repository.AmministratoreRepository;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * La classe AmministratoreHelper contiene i metodi che si occupano dell'implementazione delle logiche
 * e funzionalità vere e proprie degli endpoint richiamati dal front-end. I dati che vengono forniti a questi metodi
 * provengono dal livello "service" nel quale è stato controllato che i campi obbligatori sono stati inseriti correttamente
 * nel front-end.
 * Per "logiche e funzionalita" si intende:
 * -comunicazioni con il livello "repository" che si occuperà delle operazioni CRUD e non solo:
 * -es. controllare che un amministratore è gia presente nel sistema;
 * -es. aggiungere, cercare un amministratore.
 * -varie operazioni di logica (calcoli, operazioni, controlli generici)
 * -restituire, al front-end, le eccezioni custom in caso di errore (es. l'amministratore che vuoi inserire è già presente nel sistema)
 * -in caso di operazioni andate a buon fine, verranno restituiti al livello service i dati che dovranno essere inviati al front-end.
 */

@Component
public class AmministratoreHelper {

    private final AmministratoreRepository amministratoreRepository;
    private final HotelRepository hotelRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private static AmministratoreEnum amministratoreEnum;

    public AmministratoreHelper(AmministratoreRepository amministratoreRepository, HotelRepository hotelRepository, ClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.amministratoreRepository = amministratoreRepository;
        this.hotelRepository = hotelRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Metodo che si occupa di controllare se l'amministratore che si vuole aggiungere non è presente nel sistema.
     * In caso positivo l'amministratore verrà inserito nel database di sistema tramite il livello "repository".
     * In caso negativo verrà restituita un'eccezione custom al front-end (l'amministratore che si vuole aggiungere è presente nel sistema).
     *
     * @param amministratoreDTO
     * @return Amministratore
     */
    public Amministratore createAmministratore(AmministratoreDTO amministratoreDTO) {

        if (!amministratoreRepository.existsByUsername(amministratoreDTO.username) &&
                !clienteRepository.existsByUsername(amministratoreDTO.username)) {
            Amministratore amministratore = new Amministratore();

            amministratore.setUsername(amministratoreDTO.username);
            amministratore.setPassword(passwordEncoder.encode(amministratoreDTO.password));
            amministratore.setNome(amministratoreDTO.nome);
            amministratore.setCognome(amministratoreDTO.cognome);
            amministratore.setHotel(hotelRepository.findById(amministratoreDTO.idHotel).get());
            amministratore.setRuolo("ROLE_ADMIN");

            amministratore = amministratoreRepository.save(amministratore);

            return amministratore;
        }

        amministratoreEnum = AmministratoreEnum.getAmministratoreEnumByMessageCode("AMM_AE");
        throw new ApiRequestException(amministratoreEnum.getMessage());
    }

    /**
     * Metodo che controlla se esiste un amministratore per lo username passato dal livello "service".
     * In caso positivo restituisce l'amministratore.
     * In caso negativo restituisce un'eccezione custom (L'amministratore che stai cercando non è stato trovato)
     *
     * @param username
     * @return Amministratore
     */
    public Amministratore findAmministratoreByUsername(String username) {
        if (amministratoreRepository.existsByUsername(username)) {
            return amministratoreRepository.findByUsername(username);
        }

        amministratoreEnum = AmministratoreEnum.getAmministratoreEnumByMessageCode("AMM_NF");
        throw new ApiRequestException(amministratoreEnum.getMessage());
    }

}
