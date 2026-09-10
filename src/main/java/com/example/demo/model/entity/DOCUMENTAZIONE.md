# Package `model.entity`

**Entità JPA**: classi annotate `@Entity` mappate 1:1 sulle tabelle MySQL dello schema
`libreria`. Hibernate crea/aggiorna le tabelle all'avvio (`ddl-auto=update`).
Queste classi **non vengono mai serializzate verso il client**: per l'output si usano
i DTO in `entityDTO/`.

Alcune entità usano Lombok (`@Getter/@Setter/@NoArgsConstructor/@AllArgsConstructor`),
altre hanno costruttori e accessor scritti a mano.

---

## Entità e tabelle

| Classe | Tabella | Campi principali |
|---|---|---|
| `Libro` | `libro` | `idLibro` (PK, auto), `titoloLibro`, `annoPubblicazione`, `edizione`, `lingua` |
| `Autore` | `autore` | `idAutore` (PK), `nome`, `cognome`, `dataDiNascita`, `nazionalita`, `pseudonimo` |
| `CasaEditrice` | `casa_editrice` | `idCasaEditrice` (PK), `nomeCasaEditrice`, `nazione` |
| `Distributore` | `distributore` | `idDistributore` (PK), `nome`, `numeroTelefono` |
| `Magazzino` | `magazzino` | `idMagazzino` (PK), `indirizzo`, `quantita` |
| `MagazzinoDistributore` | `magazzino_distributore` | `idMagazzinoDistributore` (PK), `cittaMagazzino`, `indirizzo` |
| `MyUser` | *(default: `my_user`)* | `id` (PK, `GenerationType.AUTO`), `username` (col. `user_name`), `password` (hash BCrypt), `role` (stringa, ruoli separati da virgola) |

Tutte le PK numeriche usano `GenerationType.IDENTITY` (auto-increment), tranne `MyUser` (`AUTO`).

---

## Relazioni

```
CasaEditrice ──1────< n── Libro
     (Libro.idCasaEditrice  @ManyToOne  → join column  id_casa_editrice)
     (CasaEditrice.libri    @OneToMany  mappedBy = "idCasaEditrice")

Libro ──n──< autore_libro >──n── Autore
     (Libro.autore   @ManyToMany  @JoinTable "autore_libro"  [id_libro, id_autore])
     (Autore.libro   @ManyToMany  mappedBy = "autore")

Libro ──n──< libro_magazzino >──n── Magazzino
     (Libro.magazzino  @ManyToMany  @JoinTable "libro_magazzino"  [id_libro, id_magazzino])
     (Magazzino.libro  @ManyToMany  mappedBy = "magazzino")

Libro ──n──< libro_distributore >──n── Distributore
     (Libro.distributore   @ManyToMany  @JoinTable "libro_distributore"  [id_libro, id_distributore])
     (Distributore.libro   @ManyToMany  mappedBy = "distributore")

Distributore ──1────< n── MagazzinoDistributore
     (MagazzinoDistributore.distributore  @ManyToOne  → id_distributore)
     (Distributore.magazzinoDistributore  @OneToMany  mappedBy = "distributore")
```

Il lato "proprietario" di ogni many-to-many è `Libro` (definisce la `@JoinTable`);
gli altri lati usano `mappedBy`.

---

## Note operative

- `LibroService.eliminaLibro` prima di cancellare un libro fa
  `libro.getAutore().clear()` (e idem per magazzino/distributore) e salva, così
  Hibernate rimuove le righe nelle tabelle ponte ed evita la violazione di foreign
  key (MySQL error 1451).
- Sono presenti import Jackson (`@JsonManagedReference`, `@JsonBackReference`,
  `@JsonIgnore`) come residui di quando le entità venivano serializzate: oggi non
  rilevanti perché l'output passa dai DTO.
- `Autore.dataDiNascita` è `java.util.Date`; il DTO corrispondente usa `java.sql.Date`.
