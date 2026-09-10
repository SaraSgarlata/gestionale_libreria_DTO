# Package `com.example.demo`

Radice del codice applicativo del backend. Contiene la classe di avvio e i
sotto-package che realizzano l'architettura a livelli descritta in
[`backend/DOCUMENTAZIONE.md`](../../../../../../DOCUMENTAZIONE.md).

## File in questo package

| File | Ruolo |
|---|---|
| `GestionaleLibreriaDtoApplication.java` | Classe `@SpringBootApplication` con il `main`. Avvia il container Spring, la scansione dei componenti su tutto `com.example.demo`, l'auto-configurazione (web, JPA, security). |

## Sotto-package

| Package | Responsabilità | Documentazione |
|---|---|---|
| `controller/` | Espone gli endpoint HTTP (REST + pagine Thymeleaf). Traduce richieste/risposte, applica `@Valid`. | `controller/DOCUMENTAZIONE.md` |
| `service/` | Logica di business, orchestrazione, transazioni. | `service/DOCUMENTAZIONE.md` |
| `repository/` | Interfacce `JpaRepository` per l'accesso al database. | `repository/DOCUMENTAZIONE.md` |
| `model/entity/` | Entità JPA mappate sulle tabelle MySQL e loro relazioni. | `model/entity/DOCUMENTAZIONE.md` |
| `entityDTO/` | `record` DTO restituiti al client: la "vista" pubblica dei dati. | `entityDTO/DOCUMENTAZIONE.md` |
| `mapper/` | Interfacce MapStruct che convertono entità ⇄ DTO. | `mapper/DOCUMENTAZIONE.md` |
| `request/` | Classi che modellano il body delle richieste in ingresso + regole di validazione. | `request/DOCUMENTAZIONE.md` |
| `security/` | Spring Security: filtro JWT, utilità token, configurazione. | `security/DOCUMENTAZIONE.md` |
| `exception/` | Eccezione applicativa custom + handler globale `@ControllerAdvice`. | `exception/DOCUMENTAZIONE.md` |
| `config/` | Configurazione trasversale (CORS via `WebMvcConfigurer`). | `config/DOCUMENTAZIONE.md` |

## Come dialogano (richiesta tipica)

```
controller  →  service  →  repository  →  DB
     ▲            │
     └── mapper ──┘         (service usa i mapper per restituire DTO al controller)

security/JwtAuthenticationFilter  → interviene PRIMA del controller, su ogni richiesta
exception/ValidatedExceptionHandler → interviene DOPO, se qualcosa lancia RequestException
```

## Convenzioni

- **Naming in italiano** per metodi ed endpoint di dominio (`creaLibro`, `findAllListaLibriConAutore`, `/findalllibro`).
- **DTO come `record`** immutabili in `entityDTO/`.
- **Input come classi `*Request`** in `request/`, con getter/setter e annotazioni di validazione.
- **Lombok** (`@Getter/@Setter/@NoArgsConstructor/@AllArgsConstructor`) su alcune entità e request; altre hanno getter/setter scritti a mano.
- **Log** con SLF4J (`LoggerFactory.getLogger(...)`) nei service.
