package com.example.demo.controller;

import com.example.demo.request.AuthRequest;
import com.example.demo.request.AuthResponse;
import com.example.demo.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.MyUserDetailService;

// Espone l'endpoint di login (/auth/login).
// Riceve username+password, li fa validare da Spring Security (authenticationManager),
// e se corretti genera un JWT da restituire al client, che lo userà per le richieste successive.
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailService userDetailsService;

    @Autowired
    private JwtUtils jwtUtil;

    @GetMapping("/user/test")
    public String test() {
        return "token ok";
    }

    /*
     * per cercare utente su db (con postman)
     * {
     *   "username": "mario",
     *   "password": "mario",
     * }
     */
    @PostMapping("/login")
    public AuthResponse createAuthenticationToken(@RequestBody AuthRequest request) throws Exception {
        try {
            authenticationManager.authenticate(//ricevi username + password (AuthRequest).
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new Exception("Username o password non validi", e);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        //Se password corretta, generazione JWT
        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token);
    }
}
