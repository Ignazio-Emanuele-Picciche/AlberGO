package com.ignaziopicciche.albergo.security;

import com.ignaziopicciche.albergo.enums.Ruolo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JwtUtil {

    /**
     * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
     * microservices environment, this key would be kept on a config-server.
     */
    private String SECRET_KEY = "secret";

    private long validityInMilliseconds = 3600000; // 1h

    //Usa il metodo extraxtClaim per estrarre informazioni dal token
    //In questo caso estrae lo username associato al token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Usa il metodo extraxtClaim per estrarre informazioni dal token
    //In questo caso estrae la data di scadenza del token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }


    //Dice se il token è scaduto o meno
    //Semplicemente estrae la data di scadenza del token e vede se è precedente alla data corrente
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }


    //Prende il tuo nome utente e assegna un token associato a te
    public String generateToken(UserDetails userDetails, Ruolo ruolo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("auth", new SimpleGrantedAuthority(ruolo.getAuthority()));
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now) //data corrente di creazione del token
                .setExpiration(validity)  //durata del tocken (1h)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) //uso questo algoritmo per firmare la SECRET_KEY
                .compact();
    }


    //Verifica sia il nome utente associato al token e verifica anche se il token non è scauduto
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
