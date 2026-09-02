package com.example.demo.repository;

import org.springframework.data.domain.Pageable
;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.Libro;

@Repository
public interface LibroRepository extends  JpaRepository<Libro, Integer> {

	Page<Libro> findAll(Pageable pageable);
	
	
	//Page<Libro> findPaginated(int pageNo, int pageSize);
	 //Page<Libro> findByTitoloContaining(String keyword, Pageable pageable);
	



}
