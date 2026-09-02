package com.example.demo.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public class LibroRequest {

	@NotEmpty(message = "Il titolo è obbligatorio")
	private String titolo;
	 	
	private int annoPubblic;
	
	@NotEmpty
	private String edizione;
	
	@NotEmpty
	private String lingua;
	
	//private List<AutoreRequest> listaAutori;
	
	private int casaEditrice;

	
	public LibroRequest(String titolo, int annoPubblic, String edizione, String lingua,
			int casaEditrice) {  	//List<AutoreRequest> listaAutori, 			
		super();
		this.titolo = titolo;
		this.annoPubblic = annoPubblic;
		this.edizione = edizione;
		this.lingua = lingua;
		//this.listaAutori = listaAutori;
		this.casaEditrice = casaEditrice;
	}


	public LibroRequest() {
		super();
	}

	
	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public int getAnnoPubblic() {
		return annoPubblic;
	}

	public void setAnnoPubblic(int annoPubblic) {
		this.annoPubblic = annoPubblic;
	}

	public String getEdizione() {
		return edizione;
	}

	public void setEdizione(String edizione) {
		this.edizione = edizione;
	}

	public String getLingua() {
		return lingua;
	}

	public void setLingua(String lingua) {
		this.lingua = lingua;
	}

	public int getCasaEditrice() {
		return casaEditrice;
	}

	public void setCasaEditrice(int casaEditrice) {
		this.casaEditrice = casaEditrice;
	}



//	public List<AutoreRequest> getListaAutori() {
//		return listaAutori;
//	}
//
//
//	public void setListaAutori(List<AutoreRequest> listaAutori) {
//		this.listaAutori = listaAutori;
//	}
//	
	
	
	
	
	
	

}
