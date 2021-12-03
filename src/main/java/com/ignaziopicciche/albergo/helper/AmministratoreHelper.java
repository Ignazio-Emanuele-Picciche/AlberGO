package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.enums.AmministratoreEnum;
import com.ignaziopicciche.albergo.handler.ApiRequestException;
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

    private final PasswordEncoder passwordEncoder;

    private static AmministratoreEnum amministratoreEnum;

    public AmministratoreHelper(AmministratoreRepository amministratoreRepository, HotelRepository hotelRepository, PasswordEncoder passwordEncoder) {
        this.amministratoreRepository = amministratoreRepository;
        this.hotelRepository = hotelRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Long create(AmministratoreDTO amministratoreDTO){

        if(!amministratoreRepository.existsByUsername(amministratoreDTO.username)){
            Amministratore amministratore = new Amministratore();

            amministratore.setUsername(amministratoreDTO.username);
            amministratore.setPassword(passwordEncoder.encode(amministratoreDTO.password));
            amministratore.setNome(amministratoreDTO.nome);
            amministratore.setCognome(amministratoreDTO.cognome);
            amministratore.setHotel(hotelRepository.findById(amministratoreDTO.idHotel).get());

            amministratore = amministratoreRepository.save(amministratore);

            return amministratore.getId();
        }

        amministratoreEnum = AmministratoreEnum.getAmministratoreEnumByMessageCode("AMM_AE");
        throw new ApiRequestException(amministratoreEnum.getMessage());
    }

}
