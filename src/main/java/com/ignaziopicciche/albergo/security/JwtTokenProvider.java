package com.ignaziopicciche.albergo.security;

import com.ignaziopicciche.albergo.enums.Ruolo;
import com.ignaziopicciche.albergo.helper.AutenticazioneHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtTokenProvider {

    /**
     * THIS IS NOT A SECURE PRACTICE! For simplicity, we are storing a static key here. Ideally, in a
     * microservices environment, this key would be kept on a config-server.
     */
    private String SECRET_KEY = "secret";

    private long validityInMilliseconds = 3600000; // 1h

    private final AutenticazioneHelper autenticazioneHelper;

    public JwtTokenProvider(AutenticazioneHelper autenticazioneHelper) {
        this.autenticazioneHelper = autenticazioneHelper;
    }


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
    public String generateToken(UserDetails userDetails, List<Ruolo> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority())).filter(Objects::nonNull).collect(Collectors.toList()));
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


    public Authentication getAuthentication(String token) {
        UserDetails userDetails = autenticazioneHelper.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
            //throw new CustomException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
