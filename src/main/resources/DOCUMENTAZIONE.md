# `src/main/resources`

Risorse non-Java caricate dal classpath a runtime.

```
resources/
├── application.properties   → configurazione Spring Boot
└── templates/               → view Thymeleaf (UI server-side legacy)
    ├── index_libreria.html
    └── lista_libri.html
```

---

## `application.properties`

| Proprietà | Valore | Significato |
|---|---|---|
| `spring.application.name` | `gestionale_libreria` | Nome logico dell'app |
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/libreria` | Database MySQL di destinazione |
| `spring.datasource.username` | `root` | Utente DB |
| `spring.datasource.password` | *(vuota)* | Password DB |
| `spring.datasource.driver-class-name` | `com.mysql.cj.jdbc.Driver` | Driver JDBC MySQL |
| `spring.jpa.hibernate.ddl-auto` | `update` | Hibernate crea/aggiorna le tabelle mancanti all'avvio (non cancella dati) |
| `spring.jpa.show-sql` | `true` | Logga le query SQL generate |

> La porta HTTP non è specificata → default **8080**.
> Credenziali e URL sono in chiaro: per ambienti reali usare variabili d'ambiente
> o `application-<profilo>.properties` non versionati.

---

## `templates/` — Thymeleaf (interfaccia legacy)

Vecchia UI renderizzata dal server, precedente al frontend Angular. Ancora
raggiungibile ma **non è l'interfaccia ufficiale**.

### `index_libreria.html`
Home page minimale: un titolo e un link a `/listalibri`. Resa da `GET /index`
(`LibroController.homePage`).

### `lista_libri.html`
Pagina della lista libri. Resa da `GET /listalibri`. Il markup Thymeleaf è quasi
tutto commentato: i dati vengono caricati **via JavaScript** con `fetch` verso gli
endpoint del backend:

- `POST /pippo` → numero totale di pagine (per popolare il dropdown pagine);
- `POST /richiestajavascript` (body form-urlencoded `page/pageSize/field/sortDirection`)
  → pagina di libri + metadati; lo script ricostruisce le righe della tabella;
- `POST /createnewlibroconautore` (JSON) → invio del form "inserisci nuovo libro" dentro un modale.

Usa Bootstrap 5 e jQuery via CDN. Gestisce paginazione (Prev/Next + dropdown),
ordinamento ASC/DESC e apertura/chiusura del modale.

> Lo stesso comportamento (lista + inserimento) è oggi fornito, in modo più pulito,
> dai componenti Angular `lista-libri` e `form-inserimento-libro`.
