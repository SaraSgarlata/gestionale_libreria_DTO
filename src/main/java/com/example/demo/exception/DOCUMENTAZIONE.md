# Package `exception`

Gestione centralizzata degli errori applicativi: una singola eccezione di dominio e
un handler globale che la traduce in risposta HTTP uniforme.

| File | Ruolo |
|---|---|
| `RequestException` | Estende `RuntimeException`. Porta con sé un `HttpStatus`. I `service` la lanciano quando una risorsa non esiste o una regola non è rispettata, es. `throw new RequestException("Libri non trovati", HttpStatus.NOT_FOUND)`. |
| `ValidatedExceptionHandler` | `@ControllerAdvice`. Con `@ExceptionHandler(RequestException.class)` intercetta **automaticamente** ogni `RequestException` lanciata nell'app e restituisce `ResponseEntity` con corpo `{ "Status": <codice>, "Message": <messaggio> }` e lo stesso `HttpStatus` dell'eccezione. Non viene mai chiamato esplicitamente. |

## Flusso

```
Service  ──throw RequestException(msg, status)──▶  (propaga fuori dal controller)
                                                        │
                                          @ControllerAdvice intercetta
                                                        │
                                                        ▼
                              HTTP <status>  { "Status": <status>, "Message": <msg> }
```

## Non coperto qui

- Gli errori di **validazione input** (`@Valid` + `MethodArgumentNotValidException`)
  sono gestiti dal comportamento di default di Spring (`400 Bad Request`).
  Nel file è presente, commentato, un handler alternativo per formattarli campo per campo.
- Le eccezioni di autenticazione (credenziali errate al login) sono gestite in
  `AuthController` che rilancia una `Exception` generica.
