# Package `service`

Strato di **logica di business**. I service sono chiamati dai `controller`, usano i
`repository` per l'accesso ai dati e i `mapper` per produrre DTO. Qui vivono le
transazioni (`@Transactional`) e i controlli che, se falliscono, lanciano
`RequestException`.

---

## `LibroService`

Cuore del dominio "libri". Inietta `LibroRepository`, `LibroMapper`, `AutoreMapper`,
`AutoreService`. Logga con SLF4J.

### CRUD e letture
| Metodo | Cosa fa |
|---|---|
| `findAllListaLibriConAutore()` | Carica tutti i libri, li converte in `LibroDTO`. Se la lista è vuota → `RequestException(NOT_FOUND, "Libri non trovati")`. |
| `cercaLibri(String filtro)` | Lista filtrata **lato database**: filtro vuoto → `findAll()`; filtro presente → `libroRepository.cercaPerFiltro(filtro.trim())`. Converte in `LibroDTO`. **Non** lancia 404 se il risultato è vuoto (un filtro senza corrispondenze è un `200` con lista vuota). È il metodo dietro `GET /findalllibro?filtro=`. |
| `findLibroById(int id)` | Singolo libro → `LibroDTO`; 404 se assente. |
| `creaLibro(LibroRequest)` `@Transactional` | Crea un `Libro` con titolo/anno/edizione/lingua e lo salva. |
| `creaLibroConAutore(LibroRequest, AutoreRequest)` | Chiede ad `AutoreService.trovaOCreaAutorePerJSone(...)` il set di autori, crea il libro, lo collega agli autori e lo salva. Ritorna `boolean`. |
| `modificaLibro(int id, LibroRequest)` `@Transactional` | Ricarica il libro, aggiorna i campi, salva; 404 se assente. |
| `eliminaLibro(int id)` `@Transactional` | Prima **svuota** le relazioni many-to-many (`autore`, `magazzino`, `distributore`) per evitare l'errore FK 1451, poi elimina. Ritorna il `LibroDTO` eliminato. |

### Ordinamento e paginazione (a servizio della UI Thymeleaf)
| Metodo | Cosa fa |
|---|---|
| `findLibroWithSorting(String field)` | `findAll(Sort.by(ASC, field))` → lista DTO. |
| `findLibroWithPaginationAndSorting(page, pageSize, field, sortDirection)` | Costruisce `PageRequest` con `Sort` asc/desc, ritorna `Page<LibroDTO>` (ricostruita con `PageImpl`). |
| `findPaginated(Pageable, sortDirection, sortField)` | Paginazione "manuale" via `subList` sulla lista completa. |
| `getNumeroPagine()` | Totale pagine con dimensione fissa 10. |
| `pageNumbers(Page<LibroDTO>)` | Lista `[1..totalPages]` per i link di pagina. |

---

## `AutoreService`

Inietta `AutoreMapper`, `AutoreRepository`.

| Metodo | Cosa fa |
|---|---|
| `trovaOCreaAutorePerJSone(AutoreRequest)` | Cerca autori con `findByNomeContainingAndCognomeContaining`. Se non ne trova, ne crea e salva uno nuovo (nome+cognome); altrimenti riusa il primo esistente. Ritorna `Set<Autore>`. Evita duplicati di autori a ogni inserimento libro. |
| `findAllAutore()` | Tutti gli autori → `List<AutoreDTO>`; `RequestException(NOT_FOUND)` se vuoto. |

---

## `MyUserDetailService` — `implements UserDetailsService`

Ponte tra il DB utenti e Spring Security.

| Metodo | Cosa fa |
|---|---|
| `loadUserByUsername(String)` | Cerca `MyUser` via `MyUserRepository.findByUsername`. Se esiste, costruisce uno `UserDetails` (`User.builder()` con username, password hashata, ruoli). Se assente → `UsernameNotFoundException`. |
| `getRoles(MyUser)` (privato) | Se `role` è null → `["USER"]`; altrimenti fa lo `split(",")` della stringa ruoli. |

Viene invocato **automaticamente** da Spring Security in due momenti: al login e a
ogni validazione del token JWT.

---

## Note

- `creaLibroConAutore` non valorizza casa editrice / edizione come entità collegate:
  imposta solo i campi semplici del libro e il set autori.
- Le eccezioni di dominio sono sempre `RequestException` con lo `HttpStatus` adeguato,
  così l'handler globale può formattare la risposta.
