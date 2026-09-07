package com.example.demo.security;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.MyUserDetailService;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Filtro che intercetta OGNI richiesta HTTP prima che arrivi al Controller.
// Legge l'header "Authorization: Bearer <token>", estrae lo username dal token,
// verifica che il token sia valido (firma corretta + non scaduto), e se tutto ok
// "autentica" la richiesta impostando il SecurityContextHolder.
// Se il token manca o non è valido, la richiesta prosegue SENZA autenticazione
// (sarà poi SecurityConfiguration a decidere se quell'endpoint richiede login o no).

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MyUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtils.extractUsername(jwt);
            log.info("Token JWT ricevuto per l'utente: {}", username);
        } else if (authHeader != null) {
            // L'header esiste ma non ha il formato corretto (es. "Bareer", manca lo spazio, ecc.)
            log.warn("Header Authorization presente ma formato non valido: '{}'", authHeader);
        } else {
            log.debug("Nessun header Authorization nella richiesta a {}", request.getRequestURI());
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtils.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("Autenticazione riuscita per l'utente: {}", username);
            } else {
                log.warn("Token non valido o scaduto per l'utente: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}

//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @Autowired
//    private MyUserDetailService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // 1. Leggi l’header Authorization
//        final String authHeader = request.getHeader("Authorization");
//        String username = null;
//        String jwt = null;
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) { // tenta di estrarre e validare il token
//            jwt = authHeader.substring(7);
//            username = jwtUtils.extractUsername(jwt);//Estrae lo username dal token
//        }
//
//        // 2. Se username presente e nessun utente nel contesto
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            if (jwtUtils.validateToken(jwt, userDetails)) {
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails,
//                                null,
//                                userDetails.getAuthorities()
//                        );
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//
//        // 3. Continua la catena dei filtri
//        filterChain.doFilter(request, response);
//    }
//}
