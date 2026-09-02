package com.example.demo.entityDTO;

import java.util.Set;

public record LibroDTO(
		
	    String titolo,
	    int annoPubblicazione,
	    Set<String> nomiAutori
	    
	) {
	 public String nomiAutoriString() {
	        return String.join(", ", nomiAutori); //.join: unisce più stringhe in una sola stringa, 
	       										//separate da un delimitatore, virgola in questo cado
	    }
}


		
		//int idLibro,
//	    String titoloLibro;
//	    int annoPubblicazione;
	   // String edizione
	    //String lingua,
	    //String nomeCasaEditrice,            
	                  
	   // Set<String> nomiMagazzini,           
	    //Set<String> nomiDistributori 
	    
	 
//	    
//	    public LibroDTO() {
//			super();
//		}
//	    
//	    public LibroDTO(String titoloLibro, int annoPubblicazione) {
//			super();
//			this.titoloLibro = titoloLibro;
//			this.annoPubblicazione = annoPubblicazione;
//		}
//	    
//	    
//		public String getTitoloLibro() {
//			return titoloLibro;
//		}
//		
//		public void setTitoloLibro(String titoloLibro) {
//			this.titoloLibro = titoloLibro;
//		}
//		public int getAnnoPubblicazione() {
//			return annoPubblicazione;
//		}
//		public void setAnnoPubblicazione(int annoPubblicazione) {
//			this.annoPubblicazione = annoPubblicazione;
//		}
//	    
//	    
//	    
//	    
//	    
//	    
//	    
//		}
