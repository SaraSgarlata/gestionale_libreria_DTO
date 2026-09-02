package com.example.demo.model.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="casa_editrice")
public class CasaEditrice {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //per autoincrementare
	@Column(name="id_casa_editrice")
	private int idCasaEditrice;
	
	@Column(name="nome_casa_editrice")
	private String nomeCasaEditrice;
	
	@Column(name="nazione")
	private String nazione;
	
	
	@OneToMany(mappedBy = "idCasaEditrice")
	private List<Libro> libri;


}
