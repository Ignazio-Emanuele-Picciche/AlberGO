package com.ignaziopicciche.albergo.security;


import com.ignaziopicciche.albergo.helper.AutenticazioneHelper;
import com.ignaziopicciche.albergo.service.AutenticazioneService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter  {

    private final AutenticazioneHelper autenticazioneHelper;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfigurer(JwtRequestFilter jwtRequestFilter, @Lazy AutenticazioneHelper autenticazioneHelper) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.autenticazioneHelper = autenticazioneHelper;
    }


    //Autentificazione dell'utente
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(autenticazioneHelper);
    }


    /*
    Ciò che fa questo metodo è che disabilita la falsificazione delle richieste tra siti, e quindi
    autorizza le richieste /authenticate, consente a tutti di autenticarsi. e quindi qualsiasi altra richiesta
    deve essere autenticata
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/api/amministratore/login").permitAll()
                .antMatchers("/api/amministratore/register").permitAll()
                .antMatchers("/api/cliente/login").permitAll()
                .antMatchers("/api/cliente/register").permitAll()
                .antMatchers("/api/stripe/addCard").permitAll()
                .antMatchers("/api/hotel/create").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    //Codificatore di password (hashing)
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
