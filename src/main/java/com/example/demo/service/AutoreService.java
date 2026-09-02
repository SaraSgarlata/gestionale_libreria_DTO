package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entityDTO.AutoreDTO;
import com.example.demo.exception.RequestException;
import com.example.demo.mapper.AutoreMapper;
import com.example.demo.mapper.AutoreMapperImpl;
import com.example.demo.model.entity.Autore;
import com.example.demo.repository.AutoreRepository;
import com.example.demo.request.AutoreRequest;

@Service
public class AutoreService {

	@Autowired
	AutoreMapper autoreMapper;
	
	@Autowired
	AutoreRepository autoreRepository;

	
	public Set<Autore> trovaOCreaAutorePerJSone(AutoreRequest autoreRequest) {
		Set<Autore> listaAutoreEsistente = autoreRepository
				.findByNomeContainingAndCognomeContaining(autoreRequest.getNome(), autoreRequest.getCognome());
		// creo un set/lista per memorizzare gli autori
		Set<Autore> autoreList = new HashSet<Autore>();
		if (listaAutoreEsistente == null || listaAutoreEsistente.isEmpty()) {
			Autore autoreSalvato = new Autore();
			autoreSalvato.setNome(autoreRequest.getNome());
			autoreSalvato.setCognome(autoreRequest.getCognome());

			// salvo e aggiungo autore
			autoreSalvato = autoreRepository.save(autoreSalvato);
			autoreList.add(autoreSalvato);
		} else {
			// se l'autore esiste già, aggiungiamolo
			autoreList.add(listaAutoreEsistente.stream().findFirst().get()); // Prende il primo autore dalla lista
		}
		return  autoreList;
	}
	
	public List<AutoreDTO> findAllAutore ()throws RequestException{
		List<Autore> listaAutori = autoreRepository.findAll();
		if (listaAutori.isEmpty()|| listaAutori==null) {
			throw new RequestException("Libri non trovati", "404");
		}
		
		List<AutoreDTO> listaAutoriDto = new ArrayList<AutoreDTO>();
		for(Autore autore : listaAutori) {
			AutoreDTO autoreDto = autoreMapper.autoreToAutoreDto(autore);
			listaAutoriDto.add(autoreDto);
		}
		return listaAutoriDto;
		
	}

}
