package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Libro;

@Repository
public interface LibroRepository extends  JpaRepository<Libro, Integer> {

	Page<Libro> findAll(Pageable pageable);

	// Filtro unico: cerca lo stesso testo in titolo, anno di pubblicazione,
	// nome/cognome e pseudonimo degli autori. Match parziale, case-insensitive.
	// DISTINCT perché il LEFT JOIN sugli autori può duplicare le righe.
	@Query("""
			SELECT DISTINCT l FROM Libro l
			LEFT JOIN l.autore a
			WHERE LOWER(l.titoloLibro) LIKE LOWER(CONCAT('%', :filtro, '%'))
			   OR CAST(l.annoPubblicazione AS string) LIKE CONCAT('%', :filtro, '%')
			   OR LOWER(CONCAT(a.nome, ' ', a.cognome)) LIKE LOWER(CONCAT('%', :filtro, '%'))
			   OR LOWER(a.pseudonimo) LIKE LOWER(CONCAT('%', :filtro, '%'))
			""")
	List<Libro> cercaPerFiltro(@Param("filtro") String filtro);

}
