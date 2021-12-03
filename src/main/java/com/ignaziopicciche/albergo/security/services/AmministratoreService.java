package com.ignaziopicciche.albergo.security.services;

import com.google.common.base.Preconditions;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.security.helper.AmministratoreHelper;
import com.ignaziopicciche.albergo.security.models.Amministratore;
import com.ignaziopicciche.albergo.security.models.AmministratoreDTO;
import com.ignaziopicciche.albergo.security.models.AuthenticationRequest;
import com.ignaziopicciche.albergo.security.repositories.AmministratoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Objects;


//UserService
@Service
public class AmministratoreService implements UserDetailsService {

    @Autowired
    private AmministratoreHelper amministratoreHelper;

    public UserDetails loadUserByUsername(String username) {
        Preconditions.checkArgument(!Objects.isNull(username));

        return amministratoreHelper.loadUserByUsername(username);
    }

    public ResponseEntity<?> createAuthenticationToken(AuthenticationRequest authenticationRequest) throws Exception {
        Preconditions.checkArgument(!Objects.isNull(authenticationRequest.getUsername()));
        Preconditions.checkArgument(!Objects.isNull(authenticationRequest.getUsername()));

        return amministratoreHelper.createAuthenticationToken(authenticationRequest);
    }

    public Long create(AmministratoreDTO amministratoreDTO) {
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.nome));
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.cognome));
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.username));
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.password));
        Preconditions.checkArgument(!Objects.isNull(amministratoreDTO.idHotel));

        return amministratoreHelper.create(amministratoreDTO);
    }


}
