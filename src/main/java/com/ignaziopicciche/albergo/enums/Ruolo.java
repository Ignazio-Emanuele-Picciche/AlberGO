package com.ignaziopicciche.albergo.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Ruolo implements GrantedAuthority {
    ROLE_ADMIN, ROLE_CLIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
