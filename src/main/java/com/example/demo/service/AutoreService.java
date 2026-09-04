package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.entityDTO.AutoreDTO;
import com.example.demo.exception.RequestException;
import com.example.demo.mapper.AutoreMapper;
import com.example.demo.model.entity.Autore;
import com.example.demo.repository.AutoreRepository;
import com.example.demo.request.AutoreRequest;

@Service
public class AutoreService {

	@Autowired
	AutoreMapper autoreMapper;
	
	@Autowired
	AutoreRepository autoreRepository;

	private static final Logger log = LoggerFactory.getLogger(AutoreService.class);

	
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
			log.info("Autore con id {} e nome '{}' creato con successo", autoreSalvato.getIdAutore(), autoreSalvato.getNome());

		} else {
			// se l'autore esiste già, aggiungiamolo
			log.info("Autore già esistente trovato, riutilizzo il record esistente");
			autoreList.add(listaAutoreEsistente.stream().findFirst().get()); // Prende il primo autore dalla lista
		}
		return  autoreList;
	}
	
	public List<AutoreDTO> findAllAutore ()throws RequestException{
		List<Autore> listaAutori = autoreRepository.findAll();
		if (listaAutori == null || listaAutori.isEmpty()) {
			throw new RequestException("Autori non trovati", HttpStatus.NOT_FOUND);
		}
		
		List<AutoreDTO> listaAutoriDto = new ArrayList<AutoreDTO>();
		for(Autore autore : listaAutori) {
			AutoreDTO autoreDto = autoreMapper.autoreToAutoreDto(autore);
			listaAutoriDto.add(autoreDto);
		}
		return listaAutoriDto;
		
	}

}
