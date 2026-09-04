package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entityDTO.LibroDTO;
import com.example.demo.model.entity.Libro;
import com.example.demo.repository.LibroRepository;
import com.example.demo.request.LibroRequest;
import com.example.demo.request.LibroRequestData;
import com.example.demo.service.LibroService;

import org.springframework.ui.Model;
import jakarta.validation.Valid;

@Controller
public class LibroController {

	@Autowired
	LibroRepository libroRepository;

	@Autowired
	LibroService libroService;

	@GetMapping("/index")
	public String homePage() {
		return "index_libreria";
	}


	@GetMapping("/findalllibro")
	@ResponseBody
	public ResponseEntity<List<LibroDTO>> listaDeiLibri() {
		return ResponseEntity.ok(libroService.findAllListaLibriConAutore()); // Restituisce 200 ok e la lista di libri
	}

	@GetMapping("/findlibrobyid/{id}")
	@ResponseBody
	public ResponseEntity<LibroDTO> ricercaLibroDaId(@PathVariable int id) {
		LibroDTO libroTrovato = libroService.findLibroById(id);
		return ResponseEntity.ok(libroTrovato);
	}

	@PostMapping("/createnewlibro")
	@ResponseBody
	public ResponseEntity<LibroDTO> creaLibro(@Valid @RequestBody LibroRequest libroRequest) {
		// @RequestBody: Mappa il corpo della richiesta HTTP in un oggetto Java
		libroService.creaLibro(libroRequest);

		return ResponseEntity.ok(null);
	}
	
	@PostMapping("/createnewlibroconautore")
	@ResponseBody
	public ResponseEntity<Object> creaLibroMultipleObjects (@Valid @RequestBody LibroRequestData data){
		// @RequestBody: Mappa il corpo della richiesta HTTP in un oggetto Java
		boolean libroCreato = libroService.creaLibroConAutore(
				data.getLibroRequest(), 
				data.getAutoreRequest()
				);
	
		return ResponseEntity.ok(libroCreato);
	}

	@PutMapping("/modificalibroesistente/{id}")
	@ResponseBody
	public ResponseEntity<LibroDTO> modificaLibro(@PathVariable int id, @Valid @RequestBody LibroRequest libroRequest) {
		LibroDTO libroModificato = libroService.modificaLibro(id, libroRequest);
		return ResponseEntity.ok(libroModificato);
	}

	@DeleteMapping("/eliminalibroesistente/{id}")
	@ResponseBody
	public ResponseEntity<Object> eliminaLibro (@PathVariable int id){
		LibroDTO libroEliminato = libroService.eliminaLibro(id);
		Map<String, Object> response = new HashMap<>();
		response.put("messaggio", "Il libro '" + libroEliminato.titolo() + "' è stato eliminato con successo");
		response.put("libro", libroEliminato);
		return ResponseEntity.ok(response);
	}

	//////////////////////////////////
	///////////////////////////////////


	// @RequestMapping(value = "/listBooks", method = RequestMethod.GET)
	@GetMapping("/listalibri")
	public String listaLibriThyme() {

		return "lista_libri.html";
	}



	// NON IN USO
	@GetMapping("/listalibribackend")
	public String listaLibri(Model model) {
		List<LibroDTO> listaLibri = libroService.findAllListaLibriConAutore();
		model.addAttribute("tabellaListaLibri", listaLibri); // (nome a scelta, valore da passare)
		return "lista_libri";
	}

	@GetMapping("/{field}")
	@ResponseBody
	public ResponseEntity<List<LibroDTO>> getLibriWithSort(@PathVariable String field) {
		List<LibroDTO> listaLibriDto = libroService.findLibroWithSorting(field);
		return new ResponseEntity<List<LibroDTO>>(listaLibriDto, HttpStatus.OK);

	}

	//page è la pagina desiderata.
    //pageSize è il numero di libri per pagina.
    //field è il campo per applicare l'ordiname.
	@GetMapping("/pagination/{page}/{pageSize}/{field}")
	@ResponseBody
	public ResponseEntity<Page<LibroDTO>> getLibriWithPaginationAndSort(@PathVariable int page,
																		@PathVariable int pageSize, @PathVariable String field, @PathVariable String sortDirection ) {
		Page<LibroDTO> listaLibriDtoPag = libroService.findLibroWithPaginationAndSorting(page, pageSize, field, sortDirection);
		return new ResponseEntity<>(listaLibriDtoPag, HttpStatus.OK);

	}

	@PostMapping("/richiestajavascript")
	@ResponseBody
	public ResponseEntity<Object> listaJavaScript(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "titoloLibro") String field,
			@RequestParam(defaultValue = "asc") String sortDirection) {

		Map<String,Object> model = new HashMap<String, Object>();
		Page<LibroDTO> bookPage = libroService.findLibroWithPaginationAndSorting(page, pageSize, field, sortDirection);
		model.put("bookPage", bookPage);
		model.put("currentPage", page);
		model.put("pageSize", pageSize);
		model.put("sortField", field);
		model.put("pageNumbers", libroService.pageNumbers(bookPage));
		model.put("sortDirection", sortDirection);
		return new ResponseEntity<Object>(model,HttpStatus.OK);
	}


	@PostMapping("/pippo")
	@ResponseBody
	public ResponseEntity<Object> pippo (){

		Map<String,Object> model = new HashMap<String, Object>();
		model.put("numeroPagine", libroService.getNumeroPagine());
		return new ResponseEntity<Object>(model,HttpStatus.OK);
	}

}
