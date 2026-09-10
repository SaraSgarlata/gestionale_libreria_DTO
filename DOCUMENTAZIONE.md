# Backend — `gestionale_libreria_DTO`

API REST in **Spring Boot 3.4.5 / Java 17** che gestisce il catalogo della libreria e
l'autenticazione. Espone dati in JSON tramite **DTO**, persiste su **MySQL** con
**Spring Data JPA**, protegge gli endpoint con **JWT + Spring Security**.

Contiene anche una vecchia interfaccia server-side in **Thymeleaf + JavaScript**
(`/index`, `/listalibri`) rimasta come storico: la UI ufficiale è ora il frontend Angular.

---

## Come si avvia

| | |
|---|---|
| Requisiti | JDK 17, Maven (o wrapper `mvnw`), MySQL in ascolto su `localhost:3306` |
| Database | schema `libreria` (crearlo a mano); tabelle generate da Hibernate |
| Comando | `./mvnw spring-boot:run` |
| Porta | `8080` (default Spring Boot) |
| Classe main | `com.example.demo.GestionaleLibreriaDtoApplication` |

Configurazione in [`src/main/resources/application.properties`](src/main/resources/DOCUMENTAZIONE.md):
URL datasource, credenziali (`root` / password vuota), `ddl-auto=update`, `show-sql=true`.

---

## Architettura a livelli

```
            HTTP (JSON)
               │
      ┌────────▼─────────┐
      │   Controller     │  @RestController / @Controller — riceve le richieste, valida l'input (@Valid)
      └────────┬─────────┘
               │  passa Request DTO
      ┌────────▼─────────┐
      │    Service       │  @Service — logica di business, transazioni (@Transactional)
      └────────┬─────────┘
               │  usa entità + Mapper
      ┌────────▼─────────┐
      │   Repository     │  interfacce JpaRepository — query verso il DB
      └────────┬─────────┘
               │  JPA / Hibernate
      ┌────────▼─────────┐
      │     MySQL        │
      └──────────────────┘

  Trasversali:
  • Mapper (MapStruct)   entità  ⇄  DTO
  • Security (JWT)       filtro su ogni richiesta
  • Exception handling   @ControllerAdvice → risposta HTTP uniforme
  • Config (CORS)        apertura verso http://localhost:4200
```

Principio chiave: **le entità JPA non escono mai dal backend**. Ogni risposta è un
`record` DTO costruito dai Mapper; ogni input è una classe `*Request` validata.

---

## Struttura del codice

```
src/main/java/com/example/demo/
├── GestionaleLibreriaDtoApplication.java   → punto di ingresso Spring Boot
├── controller/    → endpoint REST e pagine Thymeleaf
├── service/       → logica di business
├── repository/    → accesso ai dati (Spring Data JPA)
├── model/entity/  → entità JPA mappate sulle tabelle
├── entityDTO/     → DTO (record) restituiti al client
├── mapper/        → conversione entità ⇄ DTO (MapStruct)
├── request/       → oggetti in ingresso (body delle richieste) + validazione
├── security/      → configurazione Spring Security, generazione/validazione JWT
├── exception/     → eccezione custom + handler globale
└── config/        → configurazione CORS

src/main/resources/
├── application.properties
└── templates/     → index_libreria.html, lista_libri.html (Thymeleaf legacy)
```

Dettaglio in [`src/main/java/com/example/demo/DOCUMENTAZIONE.md`](src/main/java/com/example/demo/DOCUMENTAZIONE.md).

---

## Modello dati (entità e relazioni)

```
CasaEditrice 1 ─────< n Libro                 (un libro ha una casa editrice)
Libro n >────────< n Autore                   tabella ponte  autore_libro
Libro n >────────< n Magazzino                tabella ponte  libro_magazzino
Libro n >────────< n Distributore             tabella ponte  libro_distributore
Distributore 1 ──< n MagazzinoDistributore    (sedi/magazzini di un distributore)

MyUser        → utenti applicativi (username, password BCrypt, role)
```

---

## Endpoint principali

### Autenticazione — `AuthController` / `RegistrationController`
| Metodo | Path | Auth | Descrizione |
|---|---|---|---|
| `POST` | `/register/user` | pubblico | Registra un utente (`{username, password, role}`); la password viene cifrata con BCrypt |
| `POST` | `/auth/login` | pubblico | Verifica le credenziali e restituisce `{ "token": "<JWT>" }` |
| `GET`  | `/auth/user/test` | ruolo USER | Endpoint di prova per verificare il token |

### Libri — `LibroController`
| Metodo | Path | Descrizione |
|---|---|---|
| `GET`    | `/findalllibro` | Elenco di tutti i libri come `LibroDTO` |
| `GET`    | `/findlibrobyid/{id}` | Singolo libro per id |
| `POST`   | `/createnewlibro` | Crea un libro dai soli dati libro (`LibroRequest`) |
| `POST`   | `/createnewlibroconautore` | Crea un libro + autore (`LibroRequestData`) |
| `PUT`    | `/modificalibroesistente/{id}` | Aggiorna un libro esistente |
| `DELETE` | `/eliminalibroesistente/{id}` | Elimina un libro (svuota prima le relazioni ponte) |
| `GET`    | `/{field}` | Elenco ordinato per campo (usato dalla pagina Thymeleaf) |
| `GET`    | `/pagination/{page}/{pageSize}/{field}` | Elenco paginato e ordinato |
| `POST`   | `/richiestajavascript` | Pagina di libri + metadati paginazione (per la UI Thymeleaf) |
| `POST`   | `/pippo` | Restituisce il numero di pagine totali |
| `GET`    | `/index`, `/listalibri`, `/listalibribackend` | Pagine HTML Thymeleaf legacy |

### Autori — `AutoreController`
| Metodo | Path | Descrizione |
|---|---|---|
| `GET` | `/findallautori` | Elenco autori come `AutoreDTO` |

> Regola di sicurezza: sono pubblici solo `/auth/login` e `/register/**`.
> Tutti gli altri endpoint richiedono un JWT valido (vedi `security/`).

---

## Gestione degli errori

- `RequestException` (runtime + `HttpStatus`) viene lanciata dai service quando una
  risorsa non esiste o una regola non è rispettata.
- `ValidatedExceptionHandler` (`@ControllerAdvice`) la intercetta e produce una
  risposta JSON uniforme `{ "Status": <codice>, "Message": <testo> }`.
- La validazione dell'input (`@Valid` + annotazioni `jakarta.validation`) produce
  automaticamente `400 Bad Request`.

---

## Sicurezza in breve

1. `JwtAuthenticationFilter` intercetta ogni richiesta, legge `Authorization: Bearer <token>`.
2. `JwtUtils` verifica firma (HS256) e scadenza (10 ore) ed estrae lo username.
3. `MyUserDetailService` ricarica l'utente dal DB; se il token è valido la richiesta
   viene autenticata nel `SecurityContextHolder`.
4. `SecurityConfiguration` definisce le regole: stateless (nessuna sessione), CSRF
   disabilitato, CORS verso `localhost:4200`, mappa dei permessi per path/ruolo.

Dettaglio in `src/main/java/com/example/demo/security/DOCUMENTAZIONE.md`.

> Nota: la chiave segreta JWT è hardcoded in `JwtUtils` e le credenziali DB sono in
> chiaro nel `application.properties`: accettabile per sviluppo, da esternalizzare
> (variabili d'ambiente / secret manager) prima di un rilascio reale.
