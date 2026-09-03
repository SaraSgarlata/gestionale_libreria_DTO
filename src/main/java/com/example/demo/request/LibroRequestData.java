package com.example.demo.request;

import jakarta.validation.Valid;

public class LibroRequestData {

	@Valid
	private LibroRequest libroRequest;
	@Valid
	private AutoreRequest autoreRequest;


	public LibroRequest getLibroRequest() {
		return libroRequest;
	}

	public void setLibroRequest(LibroRequest libroRequest) {
		this.libroRequest = libroRequest;
	}

	public AutoreRequest getAutoreRequest() {
		return autoreRequest;
	}

	public void setAutoreRequest(AutoreRequest autoreRequest) {
		this.autoreRequest = autoreRequest;
	}
}