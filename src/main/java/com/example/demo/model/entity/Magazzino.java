package com.example.demo.model.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name="magazzino")
public class Magazzino {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //per autoincrementare
	@Column(name="id_magazzino")
	private int idMagazzino;
	
	@Column(name="indirizzo")
	private String indirizzo;
	
	@Column(name="quantita")
	private String quantita;
	
	
	@ManyToMany(mappedBy = "magazzino")
	private Set<Libro> libro;
	

}
