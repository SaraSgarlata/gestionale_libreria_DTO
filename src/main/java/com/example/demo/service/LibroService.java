package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.entityDTO.LibroDTO;
import com.example.demo.exception.RequestException;
import com.example.demo.mapper.AutoreMapper;
import com.example.demo.mapper.LibroMapper;
import com.example.demo.model.entity.Autore;
import com.example.demo.model.entity.Libro;
import com.example.demo.repository.LibroRepository;
import com.example.demo.request.AutoreRequest;
import com.example.demo.request.LibroRequest;

import jakarta.transaction.Transactional;

@Service
public class LibroService {

	private static final Logger log = LoggerFactory.getLogger(LibroService.class);

	@Autowired
	LibroRepository libroRepository;
	@Autowired
	LibroMapper libroMapper;
	@Autowired
	AutoreMapper autoreMapper;
	
	@Autowired
	AutoreService autoreService;

	
	
	public List<LibroDTO> findAllListaLibriConAutore() throws RequestException {

		List<Libro> listaLibri = libroRepository.findAll();
		// List<Libro> listaLibri=null; //prova per debug
		if (listaLibri == null || listaLibri.isEmpty()) {
			throw new RequestException("Libri non trovati", "404");
		}

		List<LibroDTO> listaDto = new ArrayList<>();
		for (Libro libro : listaLibri) {
			LibroDTO dto = libroMapper.libroToLibroDto(libro);
			listaDto.add(dto);
		}

		return listaDto;

	}

	public List<LibroDTO> findLibroWithSorting(String field) {
		List<Libro> listaLibri = libroRepository.findAll(Sort.by(Sort.Direction.ASC, field));
		List<LibroDTO> listaDto = new ArrayList<>();
		// Converte
		for (Libro libro : listaLibri) {
			LibroDTO dto = libroMapper.libroToLibroDto(libro);
			listaDto.add(dto);
		}
		return listaDto;
	}

	//metodo per contare le pagine
	public long getNumeroPagine(){
		long tot = libroRepository.count();
		long numeroPagine= tot%10==0 ? tot/10 : (tot/10) +1;		
		return numeroPagine;
		
	}
	
	public Page<LibroDTO> findLibroWithPaginationAndSorting(int page, int pagSize, String field, String sortDirection) {
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) 
		? Sort.by(field).ascending()
		: Sort.by(field).descending();
	    Page<Libro> libriPage = libroRepository.findAll(PageRequest.of(page, pagSize, sort));

		List<LibroDTO> listaDto = new ArrayList<>();
		// Converte
		for (Libro libro : libriPage) {
			LibroDTO dto = libroMapper.libroToLibroDto(libro);
			listaDto.add(dto);
		}
		 return new PageImpl<>(listaDto, libriPage.getPageable(), libriPage.getTotalElements());

	}

	public Page<LibroDTO> findPaginated(Pageable pageable, String sortDirection, String sortField)
			throws RequestException {
//		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
//				: Sort.by(sortField).descending();
		List<Libro> listaLibri = libroRepository.findAll();
		if (listaLibri == null || listaLibri.isEmpty()) {
			throw new RequestException("Libri non trovati", "404");
		}

		int pageSize = pageable.getPageSize(); // numero di elementi per pagina
		int currentPage = pageable.getPageNumber();// nume pagina richiesta
		int startItem = currentPage * pageSize; // indice del primo elemento da visualizzare nella sottolista (es.
												// pagina 1 con 10 elementi → indice 10).
		List<Libro> subList;// lista che contiene solo le pagine da visualizzare

		if (listaLibri.size() < startItem) {
			subList = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, listaLibri.size());
			subList = listaLibri.subList(startItem, toIndex);
			// prende i libri dall’indice startItem fino a toIndex
		}
		// Converte in DTO
		List<LibroDTO> listaDto = new ArrayList<>();
		for (Libro libro : subList) {
			listaDto.add(libroMapper.libroToLibroDto(libro));
		}
		Page<LibroDTO> bookPage = new PageImpl<>(listaDto, PageRequest.of(currentPage, pageSize), listaLibri.size());
		// lista dto, info paginazione(pageRequest) e numero tot elemnti
		return bookPage;
	}

//	public Page<LibroDTO> findPaginatedDto(int pageNumber, int pageSize) {
//		Pageable pageable = (Pageable) PageRequest.of(pageNumber - 1, pageSize);
//		Page<Libro> libroPage = libroRepository.findAll(pageable);
//
//		List<LibroDTO> listaDto = new ArrayList<>();
//		for (Libro libro : libroPage.getContent()) {
//			LibroDTO dto = libroMapper.libroToLibroDto(libro);
//			listaDto.add(dto);
//		}
//		// Restituisco una nuova Page contenente i DTO
//		return new PageImpl<>(listaDto, libroPage.getPageable(), libroPage.getTotalElements());
//
//	}
//	
//	public Page<Libro> findPaginated(int pageNumber, int pageSize) {
//		Pageable pageable = (Pageable) PageRequest.of(pageNumber - 1, pageSize);
//		Page<Libro> libroPage = libroRepository.findAll(pageable);
//		return libroPage;
//	}

	public LibroDTO findLibroById(int id) throws RequestException {
		Libro libroTrovato;

		Optional<Libro> libro = libroRepository.findById(id);
		if (!libro.isPresent()) {
			throw new RequestException("libro con id " + id + " non è presente", "405");
		} else
			libroTrovato = libro.get();
		return libroMapper.libroToLibroDto(libroTrovato);

	}

	@Transactional
	public LibroDTO creaLibro(LibroRequest libroRequest) throws RequestException {

		Libro libro = new Libro();
		libro.setTitoloLibro(libroRequest.getTitolo());
		libro.setAnnoPubblicazione(libroRequest.getAnnoPubblic());
		libro.setEdizione(libroRequest.getEdizione());
		libro.setLingua(libroRequest.getLingua());		
		Libro libroSalvato = libroRepository.save(libro);
		LibroDTO libroDTO = libroMapper.libroToLibroDto(libroSalvato);
		return libroDTO ;
		
	}

	
	public boolean creaLibroConAutore(LibroRequest libroRequest, AutoreRequest autoreRequest)
					throws RequestException {
		Set<Autore> autoreList = autoreService.trovaOCreaAutorePerJSone(autoreRequest);
	
		Libro libro = new Libro();
		libro.setTitoloLibro(libroRequest.getTitolo());
		libro.setAnnoPubblicazione(libroRequest.getAnnoPubblic());
		libro.setEdizione(libroRequest.getEdizione());
		libro.setLingua(libroRequest.getLingua());

		libro.setAutore(autoreList);
		Libro libroSalvato = libroRepository.save(libro);

		LibroDTO libroDTO = libroMapper.libroToLibroDto(libroSalvato);
		log.info("Libro con id {} e titolo '{}' e autore, creato con successo", libroRequest.getTitolo(), autoreRequest.getCognome());
		return true;
	}
	
	
	@Transactional
	public LibroDTO modificaLibro(int id, LibroRequest libroRequest) throws RequestException {
		// recupero il libro tramite id
		Optional<Libro> libroEsistente = libroRepository.findById(id);
		if (libroEsistente.isPresent()) {
			Libro libro = libroEsistente.get();
			libro.setTitoloLibro(libroRequest.getTitolo());
			libro.setAnnoPubblicazione(libroRequest.getAnnoPubblic());
			libro.setEdizione(libroRequest.getEdizione());
			libro.setLingua(libroRequest.getLingua());
			Libro libroSalvato = libroRepository.save(libro);
			LibroDTO libroDTO = libroMapper.libroToLibroDto(libroSalvato);
			log.info("Libro con id {} e titolo '{}' modificato con successo", id, libroDTO.titolo());
			return libroDTO;
		} else
			throw new RequestException("libro con id " + id + " non è presente", "405");
	}


	@Transactional
	public LibroDTO eliminaLibro(int id) {
		Optional<Libro> libroEsistente = libroRepository.findById(id);
		if (libroEsistente.isPresent()){
			Libro libro = libroEsistente.get();
			LibroDTO libroDTO = libroMapper.libroToLibroDto(libro);
			// Svuoto le relazioni many-to-many per evitare errore SQL 1451 (foreign key constraint)
			libro.getAutore().clear();
			libro.getMagazzino().clear();
			libro.getDistributore().clear();

			libroRepository.save(libro);  // per aggiornare le tabelle ponte
			libroRepository.delete(libro);
			log.info("Libro con id {} e titolo '{}' eliminato con successo", id, libroDTO.titolo());
			return libroDTO;
		}else
			throw new RequestException("libro con id " + id + " non è presente", "405");
	}

	public List<Integer> pageNumbers(Page<LibroDTO> bookPage) {
		int totalPages = bookPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			return pageNumbers;
		}
		return null;

	}

}
