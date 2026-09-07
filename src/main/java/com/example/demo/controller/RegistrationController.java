package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.entity.MyUser;
import com.example.demo.repository.MyUserRepository;

@RestController
public class RegistrationController {

    @Autowired
    MyUserRepository myUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    /*
     * per registrare utente su db (con postman)
     * {
     *   "username": "mario",
     *   "password": "mario",
     *   "role": "USER"
     * }
     */
    @PostMapping("/register/user")
    public MyUser createUser(@RequestBody MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myUserRepository.save(user);
    }
}