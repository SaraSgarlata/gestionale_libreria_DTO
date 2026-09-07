package com.example.demo.service;
import java.util.Optional;
import com.example.demo.model.entity.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.repository.MyUserRepository;

// Implementazione di UserDetailsService richiesta da Spring Security.
// Il metodo loadUserByUsername viene chiamato AUTOMATICAMENTE da Spring Security
// in due momenti: durante il login (per verificare le credenziali) e durante
// la validazione del token JWT su ogni richiesta protetta (per ricaricare i dati utente).
@Service
public class MyUserDetailService implements UserDetailsService  {

    @Autowired
    MyUserRepository myUserRepository;


    //Metodo chiave loadUserByUsername: cerca l’utente nel DB tramite MyUserRepository.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = myUserRepository.findByUsername(username);

        if (user.isPresent()) {
            MyUser userObj = user.get();

            UserDetails utenteCaricato;
            utenteCaricato = User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(getRoles(userObj))
                    .build();

            return utenteCaricato;

        } else {
            throw new UsernameNotFoundException(username);
        }
    }



    private String[] getRoles (MyUser user) {
        if(user.getRole()==null) {

            return new String [] {"USER"};
        }
        return user.getRole().split(",");
    }
}