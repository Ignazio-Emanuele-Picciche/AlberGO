package com.ignaziopicciche.albergo.security;

import com.ignaziopicciche.albergo.security.filter.CustomAuthenticationFilter;
import com.ignaziopicciche.albergo.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.cors().configurationSource(r->getCorsConfiguration());


        http.authorizeRequests().antMatchers("/api/login/**").permitAll();
        http.authorizeRequests().antMatchers(POST,"/api/amministratore/register/**").permitAll();
        http.authorizeRequests().antMatchers(POST,"/api/cliente/register/**").permitAll();
        http.authorizeRequests().antMatchers(POST,"/api/stripe/addCard/**").permitAll();
        http.authorizeRequests().antMatchers(POST,"/api/hotel/create/**").permitAll();


        //AMMINISTRATORE
        http.authorizeRequests().antMatchers(GET,"/api/amministratore/dettaglio/username/**").hasAnyAuthority("ROLE_ADMIN");

        //CLIENTE
        http.authorizeRequests().antMatchers(GET,"/api/cliente/dettaglio/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER");
        http.authorizeRequests().antMatchers(GET,"/api/cliente/dettaglio/username/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET,"/api/cliente/lista/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT,"/api/cliente/update/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(DELETE,"/api/cliente/delete/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER");
        http.authorizeRequests().antMatchers(GET,"/api/cliente/searchNomeCognome/**").hasAnyAuthority("ROLE_ADMIN");


        //CATEGORIA
        http.authorizeRequests().antMatchers(GET,"/api/categoria/dettaglio/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/api/categoria/lista/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER");
        http.authorizeRequests().antMatchers(PUT,"/api/categoria/update/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/api/categoria/create/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE,"/api/categoria/delete/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/api/categoria/searchNome/**").hasAnyAuthority("ROLE_ADMIN");


        //HOTEL
        http.authorizeRequests().antMatchers(GET,"/api/hotel/dettaglio/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER");
        http.authorizeRequests().antMatchers(GET,"/api/hotel/searchNome/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET,"/api/hotel/searchIndirizzo/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET,"/api/hotel/allhotel/**").hasAnyAuthority("ROLE_USER");


        //PRENOTAZIONE
        http.authorizeRequests().antMatchers(GET,"/api/prenotazione/dettaglio/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/api/prenotazione/lista/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/api/prenotazione/stanzaId/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/api/prenotazione/create/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER");
        http.authorizeRequests().antMatchers(PUT,"/api/prenotazione/update/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER");
        http.authorizeRequests().antMatchers(DELETE,"/api/prenotazione/delete/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER");
        http.authorizeRequests().antMatchers(GET,"/api/prenotazione/listaFatture/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(GET,"/api/prenotazione/searchNomeCognomeDate/**").hasAnyAuthority("ROLE_ADMIN");


        //STANZA
        http.authorizeRequests().antMatchers(GET,"/api/stanza/dettaglio/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/api/stanza/lista/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/api/stanza/create/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT,"/api/stanza/update/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE,"/api/stanza/delete/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/api/stanza/stanzeCategoria/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/api/stanza/fuoriServizio/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/api/stanza/libere/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER");
        http.authorizeRequests().antMatchers(GET,"/api/stanza/occupate/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/api/stanza/categoriaId/**").hasAnyAuthority("ROLE_ADMIN");


        //SERVIZIO
        http.authorizeRequests().antMatchers(GET,"/api/servizio/dettaglio/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/api/servizio/lista/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET,"/api/servizio/listaNotInPrenotazione/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST,"/api/servizio/create/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT,"/api/servizio/update/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE,"/api/servizio/delete/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE,"/api/servizio/removeServizioPrenotazione/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER");
        http.authorizeRequests().antMatchers(POST,"/api/servizio/insertServizioPrenotazione/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER");
        http.authorizeRequests().antMatchers(GET,"/api/servizio/listaServiziPrenotazione/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER");


        //STRIPE
        http.authorizeRequests().antMatchers(GET,"/api/stripe/dettaglioCard/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(DELETE,"/api/stripe/deleteCard/**").hasAnyAuthority("ROLE_USER");



        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private CorsConfiguration getCorsConfiguration(){
        CorsConfiguration corsConfiguration =new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin","Access-Control-Allow-Origin","Content-Type","Accept","Authorization","Origin, Accept","X-Requesed-With","Access-Control-Request-Method","Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin","Content-Type","Accept","Authorization","Access-Control-Allow-Origin","Access-Control-Allow-Origin","Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
        return corsConfiguration;
    }

}
