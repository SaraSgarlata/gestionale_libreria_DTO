# Package `security`

Configurazione di **Spring Security** con autenticazione **stateless basata su JWT**.
Nessuna sessione server: ogni richiesta porta con sé il proprio token.

---

## Componenti

### `JwtUtils` — `@Component`
Utilità per **creare e verificare** i token.

- Chiave segreta `SECRET_KEY` = HMAC-SHA da stringa hardcoded (256 bit), algoritmo **HS256**.
- `generateToken(UserDetails)` → token con `subject = username`, `issuedAt = ora`,
  `expiration = +10 ore`.
- `extractUsername`, `extractExpiration`, `extractClaim`, `extractAllClaims` → lettura dei claim.
- `validateToken(token, userDetails)` → vero se lo username nel token combacia **e** il token non è scaduto.

### `JwtAuthenticationFilter` — `@Component`, estende `OncePerRequestFilter`
Eseguito **una volta per ogni richiesta**, prima dei controller.

1. Legge l'header `Authorization`; se inizia con `Bearer ` estrae il token.
2. Ricava lo username (`JwtUtils.extractUsername`).
3. Se c'è uno username e il `SecurityContext` è ancora vuoto:
   - ricarica l'utente con `MyUserDetailService.loadUserByUsername`;
   - se `validateToken` è ok, crea un `UsernamePasswordAuthenticationToken` e lo
     mette nel `SecurityContextHolder` → la richiesta è autenticata.
4. Se il token manca o non è valido, la catena prosegue **senza** autenticazione
   (sarà `SecurityConfiguration` a decidere se quell'endpoint la richiede).
5. Logging SLF4J di token ricevuto / header malformato / autenticazione riuscita o fallita.

### `SecurityConfiguration` — `@Configuration @EnableWebSecurity`
Definisce il `SecurityFilterChain` e i bean di supporto.

- `cors(...)` abilitato + `csrf` **disabilitato** (non serve con JWT stateless).
- **Regole di autorizzazione**:
  | Pattern | Accesso |
  |---|---|
  | `/auth/login`, `/register/**` | `permitAll` (pubblici) |
  | `/admin/**` | ruolo `ADMIN` |
  | `/user/**` | ruolo `USER` |
  | qualsiasi altro | autenticazione richiesta |
- `SessionCreationPolicy.STATELESS`.
- `addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)` →
  il controllo del token avviene per primo.
- Bean esposti:
  - `corsConfigurationSource()` → origine `http://localhost:4200`, metodi GET/POST/PUT/DELETE/OPTIONS, tutti gli header, credenziali abilitate.
  - `authenticationProvider()` → `DaoAuthenticationProvider` con `MyUserDetailService` + `BCryptPasswordEncoder`.
  - `passwordEncoder()` → `BCryptPasswordEncoder`.
  - `authenticationManager(AuthenticationConfiguration)` → usato da `AuthController` al login.

---

## Flusso completo

```
LOGIN
  POST /auth/login ──▶ AuthenticationManager.authenticate(user, pass)
                       │  (DaoAuthenticationProvider → MyUserDetailService + BCrypt)
                       ▼
                  JwtUtils.generateToken(userDetails) ──▶ { "token": "<JWT>" }

RICHIESTA PROTETTA
  GET /findalllibro
  Header: Authorization: Bearer <JWT>
        │
        ▼
  JwtAuthenticationFilter → estrae username → valida firma+scadenza → autentica il contesto
        │
        ▼
  SecurityConfiguration → "anyRequest authenticated" → OK → il controller viene eseguito
```

---

## Note di sicurezza (da rivedere prima della produzione)

- `SECRET_KEY` hardcoded nel sorgente: spostare in configurazione/secret esterno.
- Scadenza fissa 10 ore, nessun refresh token.
- I ruoli sono una stringa `role` separata da virgole sull'entità `MyUser`.
