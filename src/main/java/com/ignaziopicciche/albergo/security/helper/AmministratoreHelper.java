package com.ignaziopicciche.albergo.security.helper;

import com.ignaziopicciche.albergo.enums.AmministratoreEnum;
import com.ignaziopicciche.albergo.enums.CategoriaEnum;
import com.ignaziopicciche.albergo.enums.Ruolo;
import com.ignaziopicciche.albergo.handler.ApiRequestException;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.security.models.Amministratore;
import com.ignaziopicciche.albergo.security.models.AmministratoreDTO;
import com.ignaziopicciche.albergo.security.models.AuthenticationRequest;
import com.ignaziopicciche.albergo.security.models.AuthenticationResponse;
import com.ignaziopicciche.albergo.security.repositories.AmministratoreRepository;
import com.ignaziopicciche.albergo.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;

    private static AmministratoreEnum amministratoreEnum;

    public AmministratoreHelper(AmministratoreRepository amministratoreRepository, HotelRepository hotelRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtTokenUtil) {
        this.amministratoreRepository = amministratoreRepository;
        this.hotelRepository = hotelRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
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


    public ResponseEntity<?> createAuthenticationToken(AuthenticationRequest authenticationRequest) throws Exception {
        try { //gestisco l'eccezione in caso l'autenticazione fallisce
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails, Ruolo.ROLE_ADMIN);  //prendo il token
        return ResponseEntity.ok(new AuthenticationResponse(jwt));  //mi resituisce il token associato all'utente
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
