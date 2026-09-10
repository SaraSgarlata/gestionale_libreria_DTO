# Package `request`

Oggetti che modellano il **corpo delle richieste HTTP in ingresso** (`@RequestBody`).
Separati dai DTO di output: qui ci sono i dati che il client *invia*, con le relative
**regole di validazione** (`jakarta.validation`). I controller li ricevono con `@Valid`.

| Classe | Campi | Validazione | Usata in |
|---|---|---|---|
| `AuthRequest` | `username`, `password` | — (Lombok `@Getter/@Setter/...`) | `POST /auth/login` |
| `AuthResponse` | `token` | — | risposta di `POST /auth/login` (di fatto è un DTO di output) |
| `AutoreRequest` | `nome`, `cognome`, `dataDiNascita`, `nazionalita`, `pseudonimo` | `@NotEmpty` su nome e cognome; `@Pattern` su `dataDiNascita` (formato `yyyy-MM-dd`) | dentro `LibroRequestData` |
| `LibroRequest` | `titolo`, `annoPubblic`, `edizione`, `lingua`, `casaEditrice` (int) | `@NotEmpty` su `titolo`, `edizione`, `lingua` | `POST /createnewlibro`, `PUT /modificalibroesistente/{id}`, dentro `LibroRequestData` |
| `LibroRequestData` | `libroRequest`, `autoreRequest` (entrambi `@Valid`) | validazione a cascata | `POST /createnewlibroconautore` |

## Esempi di body

`POST /auth/login`
```json
{ "username": "mario", "password": "mario" }
```

`POST /createnewlibroconautore`
```json
{
  "libroRequest":  { "titolo": "...", "annoPubblic": 2020, "edizione": "1", "lingua": "it", "casaEditrice": 1 },
  "autoreRequest": { "nome": "Italo", "cognome": "Calvino" }
}
```

## Note

- Se la validazione fallisce Spring risponde `400 Bad Request` (gestione automatica).
- `LibroRequest.casaEditrice` è un `int` (id), ma i service attuali non lo usano per
  collegare l'entità `CasaEditrice`: il campo è previsto per un'evoluzione futura.
- Il form Angular invia `edizione` come numero e `casaEditrice` come stringa: da
  allineare ai tipi attesi da `LibroRequest` se si vuole usarli davvero.
