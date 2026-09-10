# Package `model`

Contenitore del modello di dominio persistente. Attualmente ha un solo sotto-package:

| Sotto-package | Contenuto | Documentazione |
|---|---|---|
| `entity/` | Entità JPA (`@Entity`) mappate sulle tabelle MySQL e loro relazioni. | `entity/DOCUMENTAZIONE.md` |

La separazione `model/entity` lascia spazio, in futuro, ad altri tipi di modello
(es. `model/enum`, `model/embeddable`) senza mescolarli con le entità.
