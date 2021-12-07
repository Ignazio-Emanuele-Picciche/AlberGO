package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.enums.AmministratoreEnum;
import com.ignaziopicciche.albergo.handler.ApiRequestException;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.model.Amministratore;
import com.ignaziopicciche.albergo.dto.AmministratoreDTO;
import com.ignaziopicciche.albergo.repository.AmministratoreRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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


    public Long create(AmministratoreDTO amministratoreDTO) {

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

            return amministratore.getId();
        }

        amministratoreEnum = AmministratoreEnum.getAmministratoreEnumByMessageCode("AMM_AE");
        throw new ApiRequestException(amministratoreEnum.getMessage());
    }


    public AmministratoreDTO findAmministratoreByUsername(String username) {
        if (amministratoreRepository.existsByUsername(username)) {
            Amministratore amministratore = amministratoreRepository.findByUsername(username);
            return new AmministratoreDTO(amministratore);
        }

        amministratoreEnum = AmministratoreEnum.getAmministratoreEnumByMessageCode("AMM_NF");
        throw new ApiRequestException(amministratoreEnum.getMessage());
    }

}
