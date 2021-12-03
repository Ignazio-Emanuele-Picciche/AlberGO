package com.ignaziopicciche.albergo.helper;


import com.ignaziopicciche.albergo.enums.Ruolo;
import com.ignaziopicciche.albergo.model.Amministratore;
import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.repository.AmministratoreRepository;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.security.AuthenticationRequest;
import com.ignaziopicciche.albergo.security.AuthenticationResponse;
import com.ignaziopicciche.albergo.security.JwtTokenProvider;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class AutenticazioneHelper implements UserDetailsService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private final AmministratoreRepository amministratoreRepository;
    private final ClienteRepository clienteRepository;

    public AutenticazioneHelper(AuthenticationManager authenticationManager, @Lazy JwtTokenProvider jwtTokenUtil, AmministratoreRepository amministratoreRepository, ClienteRepository clienteRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenUtil;
        this.amministratoreRepository = amministratoreRepository;
        this.clienteRepository = clienteRepository;
    }

    //TODO Da gestire l'unicità dello username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Amministratore userAmministratore = amministratoreRepository.findAmministratoreByUsername(username);
        Cliente userCliente;

        if (userAmministratore == null) {
            userCliente = clienteRepository.findClienteByUsername(username);
        } else {
            return new org.springframework.security.core.userdetails.User(userAmministratore.getUsername(), userAmministratore.getPassword(), new ArrayList<>());  //ho creato un semplice utente con username e password "foo"
        }

        if (userCliente == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(userCliente.getUsername(), userCliente.getPassword(), new ArrayList<>());  //ho creato un semplice utente con username e password "foo"
    }


    public ResponseEntity<?> createAuthenticationToken(AuthenticationRequest authenticationRequest, Ruolo ruolo) throws Exception {
        try { //gestisco l'eccezione in caso l'autenticazione fallisce
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenProvider.generateToken(userDetails, Collections.singletonList(ruolo));  //prendo il token
        return ResponseEntity.ok(new AuthenticationResponse(jwt));  //mi resituisce il token associato all'utente
    }
}
