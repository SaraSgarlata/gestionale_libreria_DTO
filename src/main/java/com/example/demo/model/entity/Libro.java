package com.example.demo.model.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="libro")
public class Libro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //per autoincrementare
	@Column(name="id_libro")
	private int idLibro;
	
	@Column(name="titolo_libro")
	private String titoloLibro;
	
	@Column(name="anno_pubblicazione")
	private int annoPubblicazione;
	
	@ManyToOne //molti libri posso avere una casa editrice
	@JoinColumn(name="id_casa_editrice")
	private CasaEditrice idCasaEditrice;
	
	@Column(name="edizione")
	private String edizione;
	
	@Column(name="lingua")
	private String lingua;

	@ManyToMany
	@JoinTable(
	        name = "autore_libro",  //nome tabella ponte
	        joinColumns = @JoinColumn(name = "id_libro"),  //colonna tab libro
	        inverseJoinColumns = @JoinColumn(name = "id_autore")  //colonna tab autore
	    )  
    private Set<Autore> autore;
		
	
	 @ManyToMany
	 @JoinTable (
			 name = "libro_magazzino",
			 joinColumns = @JoinColumn(name="id_libro"),
			 inverseJoinColumns = @JoinColumn(name= "id_magazzino")
			 )			 
	 private Set<Magazzino> magazzino;
	 
	 @ManyToMany
	    @JoinTable(
	            name = "libro_distributore", // tabella di join tra libro e distributore
	            joinColumns = @JoinColumn(name = "id_libro"),
	            inverseJoinColumns = @JoinColumn(name = "id_distributore")
	    )
	    private Set<Distributore> distributore;

	 
	 public Libro() {
			super();			
		}
	 
	public Libro(int idLibro, String titoloLibro, int annoPubblicazione, CasaEditrice idCasaEditrice, String edizione,
			String lingua, Set<Autore> autore, Set<Magazzino> magazzino, Set<Distributore> distributore) {
		super();
		this.idLibro = idLibro;
		this.titoloLibro = titoloLibro;
		this.annoPubblicazione = annoPubblicazione;
		this.idCasaEditrice = idCasaEditrice;
		this.edizione = edizione;
		this.lingua = lingua;
		this.autore = autore;
		this.magazzino = magazzino;
		this.distributore = distributore;
	}

	public int getIdLibro() {
		return idLibro;
	}

	public void setIdLibro(int idLibro) {
		this.idLibro = idLibro;
	}

	public String getTitoloLibro() {
		return titoloLibro;
	}

	public void setTitoloLibro(String titoloLibro) {
		this.titoloLibro = titoloLibro;
	}

	public int getAnnoPubblicazione() {
		return annoPubblicazione;
	}

	public void setAnnoPubblicazione(int annoPubblicazione) {
		this.annoPubblicazione = annoPubblicazione;
	}

	public CasaEditrice getIdCasaEditrice() {
		return idCasaEditrice;
	}

	public void setIdCasaEditrice(CasaEditrice idCasaEditrice) {
		this.idCasaEditrice = idCasaEditrice;
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

	public Set<Autore> getAutore() {
		return autore;
	}

	public void setAutore(Set<Autore> autore) {
		this.autore = autore;
	}

	public Set<Magazzino> getMagazzino() {
		return magazzino;
	}

	public void setMagazzino(Set<Magazzino> magazzino) {
		this.magazzino = magazzino;
	}

	public Set<Distributore> getDistributore() {
		return distributore;
	}

	public void setDistributore(Set<Distributore> distributore) {
		this.distributore = distributore;
	}


	 
	 
}
