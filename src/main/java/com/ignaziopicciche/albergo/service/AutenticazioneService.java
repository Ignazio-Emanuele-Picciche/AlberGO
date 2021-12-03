package com.ignaziopicciche.albergo.service;

import com.cookingfox.guava_preconditions.Preconditions;
import com.ignaziopicciche.albergo.enums.Ruolo;
import com.ignaziopicciche.albergo.helper.AutenticazioneHelper;
import com.ignaziopicciche.albergo.security.AuthenticationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AutenticazioneService{

    private final AutenticazioneHelper autenticazioneHelper;

    public AutenticazioneService(AutenticazioneHelper autenticazioneHelper) {
        this.autenticazioneHelper = autenticazioneHelper;
    }


    public ResponseEntity<?> createAuthenticationToken(AuthenticationRequest authenticationRequest, Ruolo ruolo) throws Exception {
        Preconditions.checkArgument(!Objects.isNull(authenticationRequest.getUsername()));
        Preconditions.checkArgument(!Objects.isNull(authenticationRequest.getUsername()));

        return autenticazioneHelper.createAuthenticationToken(authenticationRequest, ruolo);
    }

}
