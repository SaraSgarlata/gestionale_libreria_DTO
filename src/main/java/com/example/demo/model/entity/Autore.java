package com.example.demo.model.entity;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "autore")
public class Autore {


		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY) // per autoincrementare
		@Column(name = "id_autore")
		private int idAutore;

		@Column(name = "nome")
		private String nome;

		@Column(name = "cognome")
		private String cognome;

		@Column(name = "data_di_nascita")
		private Date dataDiNascita;

		@Column(name = "nazionalita")
		private String nazionalita;

		@Column(name = "pseudonimo")
		private String pseudonimo;

		@ManyToMany(mappedBy = "autore")
		private Set<Libro> libro;

		public Autore(int idAutore, String nome, String cognome, Date dataDiNascita, String nazionalita,
				String pseudonimo, Set<Libro> libro) {
			super();
			this.idAutore = idAutore;
			this.nome = nome;
			this.cognome = cognome;
			this.dataDiNascita = dataDiNascita;
			this.nazionalita = nazionalita;
			this.pseudonimo = pseudonimo;
			this.libro = libro;
		}
		public Autore() {
			super();
		}
		public int getIdAutore() {
			return idAutore;
		}
		public void setIdAutore(int idAutore) {
			this.idAutore = idAutore;
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
		public Date getDataDiNascita() {
			return dataDiNascita;
		}
		public void setDataDiNascita(Date dataDiNascita) {
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
		public Set<Libro> getLibro() {
			return libro;
		}
		public void setLibro(Set<Libro> libro) {
			this.libro = libro;
		}
		
		

	}

