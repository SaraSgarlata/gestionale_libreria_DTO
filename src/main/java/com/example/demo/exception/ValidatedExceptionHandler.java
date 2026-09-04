package com.example.demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ValidatedExceptionHandler {

// Gestore globale delle eccezioni: intercetta automaticamente ogni RequestException
// tramite @controlleradvice
// lanciata nell'app e la trasforma in una risposta HTTP con status e messaggio corretti.
// Non viene mai chiamato esplicitamente: Spring lo attiva da solo grazie a @ControllerAdvice.

	@ExceptionHandler(value = { RequestException.class })
	public ResponseEntity<Object> handleLibroRequestException(RequestException e) {
		Map<String, Object> datiException = new HashMap<>();
		datiException.put("Status", e.getHttpStatus().value());
		datiException.put("Message", e.getMessage());
		return new ResponseEntity<>(datiException, e.getHttpStatus());
	}

//	@ExceptionHandler(value = { RequestException.class })
//	public ResponseEntity<Object> handleLibroRequestException(RequestException e) {
//
//		Map<String, Object> datiException = new HashMap<String, Object>();
//		datiException.put("Status", e.getHttpStatus());
//		datiException.put("Message", e.getMessage());
//		switch (e.getHttpStatus()) {
//
//		case "522": {
//
//			return new ResponseEntity<>(datiException, HttpStatus.CONFLICT);
//		}
//		case "405": {
//
//			return new ResponseEntity<>(datiException, HttpStatus.BAD_REQUEST);
//		}
//		case "444": {
//
//			return new ResponseEntity<>(datiException, HttpStatus.BAD_REQUEST);
//		}
//
//		default:
//			return new ResponseEntity<>(datiException, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

//
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
//		Map<String, String> errors = new HashMap<>();
//
//		ex.getBindingResult().getAllErrors().forEach((error) -> {
//			String fieldName = ((FieldError) error).getField();
//			String errorMessage = error.getDefaultMessage();
//
//			errors.put(fieldName, errorMessage);
//		});
//		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//	}
}
