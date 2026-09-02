package com.example.demo.entityDTO;

import java.util.Set;

public record MagazzinoDTO(
		
		 int idMagazzino,	
		 String indirizzo,	
		 String quantita,						
		 Set<String> setMagazzinoLibro
		
		) {

}
