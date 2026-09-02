package com.example.demo.entityDTO;

import java.util.Set;

public record CasaEditriceDTO(
		
		 int idCasaEditrice,		
		 String nomeCasaEditrice,	
		 String nazione,
		 Set<String> nomiLibri
		) {

}
