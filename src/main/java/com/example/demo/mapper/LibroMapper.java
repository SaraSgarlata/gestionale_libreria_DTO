package com.example.demo.mapper;

import java.util.HashSet;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.demo.entityDTO.LibroDTO;
import com.example.demo.model.entity.Autore;
import com.example.demo.model.entity.Libro;

@Mapper(componentModel = "spring")
public interface LibroMapper {
	// MAPPER: converte oggetti da un tipo all'altro in modo automatico,
	// generando il codice necessario al momento della compilazione

	@Mapping(source = "titoloLibro", target = "titolo")
	@Mapping(source = "annoPubblicazione", target = "annoPubblicazione")
	@Mapping(source = "autore", target = "nomiAutori", qualifiedByName = "mapAutoreToNomiAutori")
	LibroDTO libroToLibroDto(Libro libro);

	// conversione inversa DTO -> entity (nel progetto utilizzo libroRequest)
	@Mapping(source = "titolo", target = "titoloLibro")
	Libro libroDtoToLibro(LibroDTO dto);

	// Metodo personalizzato per concatenare il nome e il cognome dell'autore e aggiungere il pseudonimo
	@Named("mapAutoreToNomiAutori")
	default Set<String> mapAutoreToNomiAutori(Set<Autore> autori) {
		Set<String> nomiAutori = new HashSet<>();
		
		for (Autore autore : autori) {
			String pseudonimo = autore.getPseudonimo() != null ? autore.getPseudonimo() : " N.P.";
			nomiAutori.add(autore.getNome() + " " + autore.getCognome() + " Pseudonimo: " + pseudonimo);
		}
		return nomiAutori;
	}


}
