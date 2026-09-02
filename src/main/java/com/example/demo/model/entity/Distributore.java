package com.example.demo.model.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "distributore")
public class Distributore {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //per autoincrementare
	@Column(name = "id_distributore")
	private int idDistributore;

	@Column(name = "nome")
	private String nome;

	@Column(name = "numero_telefono")
	private String numeroTelefono;

	@OneToMany(mappedBy = "distributore")
	private Set<MagazzinoDistributore> magazzinoDistributore;


	@ManyToMany(mappedBy = "distributore") 
    private Set<Libro> libro; 
	

}
