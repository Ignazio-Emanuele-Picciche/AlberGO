package com.ignaziopicciche.albergo.service;

import com.ignaziopicciche.albergo.model.Amministratore;
import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.repository.AmministratoreRepository;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final AmministratoreRepository amministratoreRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Amministratore amministratore = amministratoreRepository.findByUsername(username);

        Cliente cliente = null;
        if (amministratore == null) {
            cliente = clienteRepository.findByUsername(username);
        }


        if (amministratore == null && cliente == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (amministratore != null) {
            authorities.add(new SimpleGrantedAuthority(amministratore.getRuolo()));
            return new org.springframework.security.core.userdetails.User(amministratore.getUsername(), amministratore.getPassword(), authorities);
        } else {
            authorities.add(new SimpleGrantedAuthority(cliente.getRuolo()));
            return new org.springframework.security.core.userdetails.User(cliente.getUsername(), cliente.getPassword(), authorities);
        }
    }

}
