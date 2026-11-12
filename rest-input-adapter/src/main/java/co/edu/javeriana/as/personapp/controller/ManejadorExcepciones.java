package co.edu.javeriana.as.personapp.controller;

import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;

// Traduce las excepciones de la aplicación a códigos HTTP para todos los controllers
@RestControllerAdvice
public class ManejadorExcepciones {

	@ExceptionHandler(InvalidOptionException.class)
	public ResponseEntity<String> baseInvalida(InvalidOptionException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@ExceptionHandler(NoExistException.class)
	public ResponseEntity<String> noExiste(NoExistException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler(AlreadyExistException.class)
	public ResponseEntity<String> yaExiste(AlreadyExistException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}

	// Números o fechas mal formados en el request
	@ExceptionHandler({ IllegalArgumentException.class, DateTimeParseException.class })
	public ResponseEntity<String> datosInvalidos(RuntimeException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos inválidos: " + e.getMessage());
	}
}
