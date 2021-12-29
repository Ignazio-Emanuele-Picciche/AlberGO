package com.ignaziopicciche.albergo.service.impl;

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

/**
 * Il metodo della classe UserService viene richiamato dalla classe SecurityConfig.
 * Questo metodo ha il compito di controllare che l'utente (cliente o amministratore) che effettuare il login è
 * presente nel sistema. Se non è presente vengono restituiti dei log ed eccezioni di errore.
 * In caso positivo, invece, viene effettuata tutta la logica di login, presente nel package Security.
 */

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserDetailsService {

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
