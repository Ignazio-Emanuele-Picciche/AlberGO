package com.ignaziopicciche.albergo.security.helper;

import com.ignaziopicciche.albergo.enums.AmministratoreEnum;
import com.ignaziopicciche.albergo.enums.CategoriaEnum;
import com.ignaziopicciche.albergo.handler.ApiRequestException;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.security.models.Amministratore;
import com.ignaziopicciche.albergo.security.models.AmministratoreDTO;
import com.ignaziopicciche.albergo.security.repositories.AmministratoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AmministratoreHelper implements UserDetailsService {

    private final AmministratoreRepository amministratoreRepository;
    private final HotelRepository hotelRepository;
    private final PasswordEncoder passwordEncoder;

    private static AmministratoreEnum amministratoreEnum;

    public AmministratoreHelper(AmministratoreRepository amministratoreRepository, HotelRepository hotelRepository, PasswordEncoder passwordEncoder) {
        this.amministratoreRepository = amministratoreRepository;
        this.hotelRepository = hotelRepository;
        this.passwordEncoder = passwordEncoder;
    }


    //TODO Da gestire l'unicità dello username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Amministratore user = amministratoreRepository.findAmministratoreByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User not found with username: "+username);
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());  //ho creato un semplice utente con username e password "foo"
    }


    public AmministratoreDTO save(AmministratoreDTO userDTO){

        if(!amministratoreRepository.existsByUsername(userDTO.username)){
            Amministratore newUser = new Amministratore();

            newUser.setUsername(userDTO.username);
            newUser.setPassword(passwordEncoder.encode(userDTO.password));
            newUser.setNome(userDTO.nome);
            newUser.setCognome(userDTO.cognome);
            newUser.setHotel(hotelRepository.findById(userDTO.idHotel).get());

            amministratoreRepository.save(newUser);

            return new AmministratoreDTO(newUser);
        }

        amministratoreEnum = AmministratoreEnum.getAmministratoreEnumByMessageCode("AMM_AE");
        throw new ApiRequestException(amministratoreEnum.getMessage());
    }

}
