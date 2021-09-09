package com.ignaziopicciche.albergo.security.services;

import com.google.common.base.Preconditions;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.security.helper.AmministratoreHelper;
import com.ignaziopicciche.albergo.security.models.Amministratore;
import com.ignaziopicciche.albergo.security.models.AmministratoreDTO;
import com.ignaziopicciche.albergo.security.repositories.AmministratoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;


//UserService
@Service
public class AmministratoreService implements UserDetailsService {

    @Autowired
    private AmministratoreHelper amministratoreHelper;

    public UserDetails loadUserByUsername(String username){
        Preconditions.checkArgument(!Objects.isNull(username));

        return amministratoreHelper.loadUserByUsername(username);
    }


    public AmministratoreDTO save(AmministratoreDTO userDTO){
        Preconditions.checkArgument(!Objects.isNull(userDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(userDTO.cognome));
        Preconditions.checkArgument(!Objects.isNull(userDTO.username));
        Preconditions.checkArgument(!Objects.isNull(userDTO.password));
        Preconditions.checkArgument(!Objects.isNull(userDTO.idHotel));

        return amministratoreHelper.save(userDTO);
    }



}
