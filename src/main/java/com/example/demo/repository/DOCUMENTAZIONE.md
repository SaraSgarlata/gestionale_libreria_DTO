# Package `repository`

Strato di **accesso ai dati**. Sono interfacce che estendono `JpaRepository`:
Spring Data JPA ne genera a runtime l'implementazione con i metodi CRUD standard
(`findAll`, `findById`, `save`, `delete`, `count`, paginazione, ordinamento).
Si aggiungono solo le **query derivate** necessarie (dedotte dal nome del metodo).

| Repository | Entità / chiave | Metodi aggiuntivi |
|---|---|---|
| `LibroRepository` | `Libro` / `Integer` | `Page<Libro> findAll(Pageable pageable)` — dichiarato esplicitamente per la paginazione (di fatto già fornito da `JpaRepository`). `List<Libro> cercaPerFiltro(String filtro)` — `@Query` JPQL con `LEFT JOIN` sugli autori: un unico termine cercato (LIKE, case-insensitive) su titolo, anno di pubblicazione (`CAST(... AS string)`), nome+cognome e pseudonimo autore; `DISTINCT` per deduplicare le righe generate dal join. Annotato `@Repository`. |
| `AutoreRepository` | `Autore` / `Integer` | `Set<Autore> findByNomeContainingAndCognomeContaining(String nome, String cognome)` — ricerca "fuzzy" su nome **e** cognome (LIKE %...%). Usato da `AutoreService` per trovare-o-creare un autore. |
| `MyUserRepository` | `MyUser` / `Long` | `Optional<MyUser> findByUsername(String username)` — usato in fase di login/validazione token. Annotato `@Repository`. |

## Come vengono usati

```
LibroService     → LibroRepository     (CRUD libri, paginazione, ordinamento, count)
AutoreService    → AutoreRepository    (ricerca autori esistenti, salvataggio)
MyUserDetailService / RegistrationController → MyUserRepository (lookup e registrazione utenti)
```

## Note

- Unica query scritta a mano: `LibroRepository.cercaPerFiltro` (`@Query` JPQL), usata
  dalla ricerca libri del frontend. Il resto è metodi standard o derivati dal nome.
- La paginazione lato DB (`PageRequest.of(...)`) è usata solo dagli endpoint Thymeleaf;
  il frontend Angular pagina lato client (ma **filtra lato server** via `cercaPerFiltro`).
