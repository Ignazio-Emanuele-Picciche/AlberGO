package com.ignaziopicciche.albergo.security;

import lombok.Data;

@Data
public class AuthenticationResponse {

    private final String jwt;

    public AuthenticationResponse(String jwt){
        this.jwt = jwt;
    }

}
