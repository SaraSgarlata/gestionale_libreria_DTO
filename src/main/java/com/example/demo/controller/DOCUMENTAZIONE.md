# Package `controller`

Strato di ingresso HTTP. Ogni classe qui riceve le richieste, delega ai `service` e
restituisce la risposta. Non contiene logica di business.

Due stili convivono:
- **REST** (`@RestController` o `@Controller` + `@ResponseBody`) → JSON per il frontend Angular;
- **MVC/Thymeleaf** (`@Controller` che ritorna il nome di una view) → pagine HTML legacy.

---

## `AuthController` — `@RestController`, base path `/auth`

| Endpoint | Descrizione |
|---|---|
| `POST /auth/login` | Riceve `AuthRequest` (username, password). Le fa validare da `AuthenticationManager`; se ok, `MyUserDetailService` carica l'utente e `JwtUtils` genera il token. Risponde con `AuthResponse { token }`. In caso di credenziali errate lancia un'eccezione ("Username o password non validi"). |
| `GET /auth/user/test` | Ritorna la stringa `"token ok"`. Serve solo a verificare che un token con ruolo USER sia accettato. |

Dipendenze: `AuthenticationManager`, `MyUserDetailService`, `JwtUtils`.

---

## `RegistrationController` — `@RestController`

| Endpoint | Descrizione |
|---|---|
| `POST /register/user` | Riceve un `MyUser` (`{username, password, role}`), cifra la password con `PasswordEncoder` (BCrypt) e la salva tramite `MyUserRepository`. Endpoint **pubblico**. |

---

## `AutoreController` — `@Controller`

| Endpoint | Descrizione |
|---|---|
| `GET /findallautori` | `@ResponseBody`. Ritorna `List<AutoreDTO>` da `AutoreService.findAllAutore()`. |

---

## `LibroController` — `@Controller`

Il controller più ricco. Inietta `LibroRepository` e `LibroService`.

### Endpoint REST (usati dal frontend / da client JSON)

| Endpoint | Metodo service | Note |
|---|---|---|
| `GET /findalllibro` | `cercaLibri(filtro)` | Lista come `LibroDTO`. Query param opzionale `?filtro=<testo>`: se assente ritorna tutti i libri, se presente la ricerca (titolo / anno / autori) è eseguita nella query SQL. Ritorna `200` con lista eventualmente vuota. |
| `GET /findlibrobyid/{id}` | `findLibroById(id)` | 404 (`RequestException`) se assente. |
| `POST /createnewlibro` | `creaLibro(LibroRequest)` | Solo dati libro. Body validato con `@Valid`. |
| `POST /createnewlibroconautore` | `creaLibroConAutore(...)` | Body `LibroRequestData` = `LibroRequest` + `AutoreRequest`. È l'endpoint usato dal form Angular. |
| `PUT /modificalibroesistente/{id}` | `modificaLibro(id, LibroRequest)` | Aggiorna titolo, anno, edizione, lingua. |
| `DELETE /eliminalibroesistente/{id}` | `eliminaLibro(id)` | Risponde con messaggio + libro eliminato. |

### Endpoint di supporto alla UI Thymeleaf legacy

| Endpoint | Scopo |
|---|---|
| `GET /index` | Ritorna la view `index_libreria`. |
| `GET /listalibri` | Ritorna la view `lista_libri.html` (che poi carica i dati via fetch JS). |
| `GET /listalibribackend` | Variante che passa la lista già nel `Model` (non in uso). |
| `GET /{field}` | `findLibroWithSorting(field)` — lista ordinata per campo. |
| `GET /pagination/{page}/{pageSize}/{field}` | `findLibroWithPaginationAndSorting(...)` — `Page<LibroDTO>`. |
| `POST /richiestajavascript` | Ritorna una mappa con `bookPage`, `currentPage`, `pageSize`, `sortField`, `sortDirection`, `pageNumbers`. Consumato dallo script in `lista_libri.html`. |
| `POST /pippo` | Ritorna `{ numeroPagine }` (totale pagine). |

> Attenzione: `GET /{field}` è una rotta "catch-all" a un segmento; convive con gli
> altri path grazie all'ordine di match di Spring, ma è un design fragile.

---

## Flusso di una richiesta

```
Client → [JwtAuthenticationFilter] → Controller.metodo(@Valid Request)
                                          │
                                          ▼
                                    Service (business + @Transactional)
                                          │
                                   Mapper (entità → DTO)
                                          ▼
                              ResponseEntity<DTO>  → JSON
```

Se il service lancia `RequestException`, la risposta viene formattata da
`exception/ValidatedExceptionHandler`.
