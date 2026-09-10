# Package `mapper`

Interfacce **MapStruct** che convertono le entità JPA in DTO (e viceversa).
MapStruct genera l'implementazione a **compile-time** (annotation processor
configurato nel `pom.xml`); `componentModel = "spring"` fa sì che le implementazioni
generate siano bean Spring iniettabili con `@Autowired`.

---

## `LibroMapper`

| Metodo | Direzione | Mapping |
|---|---|---|
| `libroToLibroDto(Libro)` | entità → `LibroDTO` | `titoloLibro → titolo`, `annoPubblicazione → annoPubblicazione`, `autore → nomiAutori` (via metodo custom) |
| `libroDtoToLibro(LibroDTO)` | `LibroDTO` → entità | `titolo → titoloLibro` (conversione inversa, poco usata: in scrittura si usano le `*Request`) |
| `mapAutoreToNomiAutori(Set<Autore>)` `@Named` | helper | Per ogni autore produce la stringa `"<nome> <cognome> Pseudonimo: <pseudonimo|N.P.>"` e la raccoglie in un `Set<String>` |

## `AutoreMapper`

| Metodo | Direzione | Mapping |
|---|---|---|
| `autoreToAutoreDto(Autore)` | entità → `AutoreDTO` | `nome → nomeAutore` (gli altri campi con nome uguale sono automatici) |
| `autoreDtoToAutore(AutoreDTO)` | `AutoreDTO` → entità | `nomeAutore → nome` |

---

## Dove vengono usati

```
LibroService.findAll... / creaLibro / modificaLibro / eliminaLibro → libroMapper.libroToLibroDto(...)
AutoreService.findAllAutore                                        → autoreMapper.autoreToAutoreDto(...)
```

## Note

- Il codice generato finisce in `target/generated-sources/annotations/` (build Maven).
- Se si aggiunge un campo a un DTO senza corrispondente nell'entità, la compilazione
  MapStruct fallisce (o avvisa): utile come "contratto" verificato dal compilatore.
