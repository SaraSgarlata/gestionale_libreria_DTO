package com.example.demo.security;


import com.example.demo.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Configurazione centrale di Spring Security per l'intera applicazione.
// - Disabilita CSRF (non necessario con JWT stateless, niente cookie di sessione)
// - Definisce quali endpoint sono pubblici (permitAll) e quali richiedono autenticazione
// - SessionCreationPolicy.STATELESS: nessuna sessione salvata sul server,
//   ogni richiesta si autentica da sola tramite il token JWT
// - Registra JwtAuthenticationFilter PRIMA del filtro standard di Spring Security,
//   così il controllo del token avviene per primo su ogni richiesta
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    @Autowired
    private MyUserDetailService myUserDetailService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorization -> {
                    authorization.requestMatchers("/auth/login", "/register/**").permitAll();
                    authorization.requestMatchers("/admin/**").hasRole("ADMIN");
                    authorization.requestMatchers("/user/**").hasRole("USER");
                    authorization.anyRequest().authenticated();
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //succede qualcos
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*
	 * Il  metodo authenticationProvider() crea un provider che, quando Spring Security riceve dati di login:
	usa la tua implementazione di UserDetailsService per trovare l’utente,
	usa il PasswordEncoder per controllare la password e decide se l’utente è autenticato o meno.
	Quindi anche se nel metodo tu fai solo un set sul provider, internamente Spring Security
	fa la catena che chiama loadUserByUsername() al momento dell'autenticazione.
	 */
    // uso MyUserDetailService e il password encoder per l'autenticazione.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
