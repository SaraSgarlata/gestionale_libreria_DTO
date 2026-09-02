package com.example.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name="magazzino_distributore")
public class MagazzinoDistributore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //per autoincrementare
	@Column(name="id_magazzino_distributore")
	private int idMagazzinoDistributore;
	
	@Column(name="citta_magazzino")
	private String cittaMagazzino;
	
	@Column(name="indirizzo")
	private String indirizzo;	
											//many to one a distributore, perchè:
	@ManyToOne								//molti Magazzini-Distributori hanno un solo distrubutore
	@JoinColumn(name = "id_distributore")   //es: in via genova ci possono essere piu magazzini(?)
	private Distributore distributore;
	    


}
