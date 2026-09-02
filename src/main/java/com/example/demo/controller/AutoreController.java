package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entityDTO.AutoreDTO;
import com.example.demo.service.AutoreService;

@Controller
public class AutoreController {
	
	@Autowired
	AutoreService autoreService;
	
	@GetMapping("/findallautori")
	@ResponseBody
	public ResponseEntity<List<AutoreDTO>> listaAutori (){
		return ResponseEntity.ok(autoreService.findAllAutore());
		
	}

}
