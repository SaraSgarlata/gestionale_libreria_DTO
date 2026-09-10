# Package `entityDTO`

**Data Transfer Object**: `record` Java immutabili che rappresentano la "vista"
pubblica dei dati restituita al client. Isolano il frontend dalla struttura interna
delle entità JPA (nessuna relazione lazy, nessun ciclo di serializzazione, solo i
campi utili).

Vengono costruiti dai `mapper/` a partire dalle entità.

| DTO | Campi | Usato da |
|---|---|---|
| `LibroDTO` | `titolo`, `annoPubblicazione`, `Set<String> nomiAutori` + metodo `nomiAutoriString()` (unisce gli autori in una stringa separata da virgole) | `GET /findalllibro`, `GET /findlibrobyid/{id}`, endpoint di paginazione/ordinamento |
| `AutoreDTO` | `nomeAutore`, `cognome`, `dataDiNascita` (`java.sql.Date`), `nazionalita`, `pseudonimo`, `Set<String> nomiLibri` | `GET /findallautori` |
| `CasaEditriceDTO` | `idCasaEditrice`, `nomeCasaEditrice`, `nazione`, `Set<String> nomiLibri` | *(non ancora esposto da un endpoint)* |
| `DistributoreDTO` | `idDistributore`, `nome`, `numeroTelefono`, `Set<String> libriDistribuiti` | *(non ancora esposto)* |
| `MagazzinoDTO` | `idMagazzino`, `indirizzo`, `quantita`, `Set<String> setMagazzinoLibro` | *(non ancora esposto)* |
| `MagazzinoDistributoreDTO` | `idMagazzinoDistributore`, `cittaMagazzino`, `indirizzo`, `idDistributore` | *(non ancora esposto)* |

## Perché `record`

- Immutabilità: una volta creato il DTO non cambia.
- Sintassi compatta: campi, costruttore, `equals`/`hashCode`/`toString` generati.
- Gli accessor non hanno il prefisso `get` (`libroDTO.titolo()`, non `getTitolo()`);
  è il motivo per cui `LibroMapper` mappa verso `titolo` e in `LibroController` si
  legge `libroEliminato.titolo()`.

## Corrispondenza con il frontend

`LibroDTO` ↔ interfaccia TypeScript `Libro` in
`frontend/src/app/models/libro.ts` (`titolo`, `annoPubblicazione`, `nomiAutori`).
