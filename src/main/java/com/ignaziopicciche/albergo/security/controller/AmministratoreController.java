package com.ignaziopicciche.albergo.security.controller;


import com.ignaziopicciche.albergo.security.models.AuthenticationRequest;
import com.ignaziopicciche.albergo.security.models.AuthenticationResponse;
import com.ignaziopicciche.albergo.security.models.AmministratoreDTO;
import com.ignaziopicciche.albergo.security.services.AmministratoreService;
import com.ignaziopicciche.albergo.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/amministratore")
public class AmministratoreController {

    private final AmministratoreService amministratoreService; //UserService

    public AmministratoreController(AmministratoreService amministratoreService) {
        this.amministratoreService = amministratoreService;
    }


    //Endpoint di autenticazione che prende ID utente e password e poi ritorna a JWT
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return amministratoreService.createAuthenticationToken(authenticationRequest);
    }


    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody AmministratoreDTO amministratoreDTO) {
        return ResponseEntity.ok(amministratoreService.create(amministratoreDTO));
    }


}
