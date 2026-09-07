package com.example.demo.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;


// Classe di utilità per generare e validare i token JWT.
// Il token contiene: username (subject), data di emissione, data di scadenza (10 ore),
// firmato con una chiave segreta (SECRET_KEY) usando l'algoritmo HS256.
// Nessuno può modificare il contenuto del token senza invalidare la firma.
@Component
public class JwtUtils {

    private final Key SECRET_KEY = Keys.hmacShaKeyFor("secret123secret123secret123secret123".getBytes()); // deve essere lunga almeno 256 bit
    //private final String SECRET_KEY = "secret123"; // in produzione deve essere più complesso e sicuro

    // Estrae username dal token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Estrae data di scadenza
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Estrae un qualsiasi claim dal token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Controlla se il token è scaduto
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Genera un token per l'utente
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // puoi aggiungere claims personalizzati qui se vuoi
        return createToken(claims, userDetails.getUsername());
    }


    //claims sono delle informazioni opzionali che si possono aggiungere al token
    private String createToken(Map<String, Object> claims, String subject) {
        long expirationTime = 1000 * 60 * 60 * 10; // 10 oree
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Controlla se il token è valido
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
