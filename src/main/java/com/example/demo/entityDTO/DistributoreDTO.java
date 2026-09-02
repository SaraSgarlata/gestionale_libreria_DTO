package com.example.demo.entityDTO;

import java.util.Set;

public record DistributoreDTO(
		
			int idDistributore,
		    String nome,
		    String numeroTelefono,
		    Set<String> libriDistribuiti  
		) {

}
