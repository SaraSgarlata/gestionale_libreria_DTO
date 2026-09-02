package com.example.demo.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.entity.Autore;

public interface AutoreRepository extends JpaRepository<Autore, Integer> {

	
	 public Set<Autore> findByNomeContainingAndCognomeContaining(String nome, String cognome);

}
