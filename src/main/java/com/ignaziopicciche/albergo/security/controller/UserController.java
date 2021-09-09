package com.ignaziopicciche.albergo.security.controller;


import com.ignaziopicciche.albergo.security.models.AuthenticationRequest;
import com.ignaziopicciche.albergo.security.models.AuthenticationResponse;
import com.ignaziopicciche.albergo.security.models.UserDTO;
import com.ignaziopicciche.albergo.security.services.UserService;
import com.ignaziopicciche.albergo.security.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userDetailsService; //UserService

    @Autowired
    private JwtUtil jwtTokenUtil;


    @RequestMapping("/hello")
    public String hello() {
        return "Hello world!";
    }

    //Endpoint di autenticazione che prende ID utente e password e poi ritorna a JWT
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try { //gestisco l'eccezione in caso l'autenticazione fallisce
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }


        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);  //prendo il token

        return ResponseEntity.ok(new AuthenticationResponse(jwt));  //mi resituisce il token associato all'utente
    }


    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception{
        return ResponseEntity.ok(userDetailsService.save(user));
    }










}
