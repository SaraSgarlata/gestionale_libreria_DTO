package com.example.demo.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class AutoreRequest {
	
	@NotEmpty	
	private String nome;
	
	@NotEmpty
	private String cognome;
	
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "formato data non valido, required: 'yyyy-MM-dd'")
	private String dataDiNascita;
	
	@NotEmpty
	private String nazionalita;
	
	@NotEmpty
	private String pseudonimo;

	public AutoreRequest(@NotEmpty String nome, @NotEmpty String cognome, String dataDiNascita,
			@NotEmpty String nazionalita, @NotEmpty String pseudonimo) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.dataDiNascita = dataDiNascita;
		this.nazionalita = nazionalita;
		this.pseudonimo = pseudonimo;
	}

	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getDataDiNascita() {
		return dataDiNascita;
	}

	public void setDataDiNascita(String dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}

	public String getNazionalita() {
		return nazionalita;
	}

	public void setNazionalita(String nazionalita) {
		this.nazionalita = nazionalita;
	}

	public String getPseudonimo() {
		return pseudonimo;
	}

	public void setPseudonimo(String pseudonimo) {
		this.pseudonimo = pseudonimo;
	}
	
	
	

}
