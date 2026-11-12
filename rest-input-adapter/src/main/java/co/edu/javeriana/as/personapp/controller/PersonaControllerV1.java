package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
public class PersonaControllerV1 {
	
	@Autowired
	private PersonaInputAdapterRest personaInputAdapterRest;
	
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PersonaResponse> personas(@PathVariable String database) throws InvalidOptionException {
		log.info("Into personas REST API");
			return personaInputAdapterRest.historial(database.toUpperCase());
	}
	
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public PersonaResponse crearPersona(@RequestBody PersonaRequest request)
			throws InvalidOptionException, AlreadyExistException {
		log.info("esta en el metodo crearTarea en el controller del api");
		return personaInputAdapterRest.crearPersona(request);
	}

	@ResponseBody
	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> contarPersonas(@PathVariable String database) {
		try {
			return ResponseEntity.ok(personaInputAdapterRest.contarPersonas(database.toUpperCase()));
		} catch (InvalidOptionException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@ResponseBody
	@GetMapping(path = "/{database}/{identification}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> buscarPersona(@PathVariable String database, @PathVariable Integer identification) {
		try {
			return ResponseEntity.ok(personaInputAdapterRest.buscarPersona(database.toUpperCase(), identification));
		} catch (InvalidOptionException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NoExistException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@ResponseBody
	@PutMapping(path = "/{database}/{identification}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> editarPersona(@PathVariable String database, @PathVariable Integer identification,
			@RequestBody PersonaRequest request) {
		try {
			return ResponseEntity.ok(personaInputAdapterRest.editarPersona(database.toUpperCase(), identification, request));
		} catch (InvalidOptionException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NoExistException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@ResponseBody
	@DeleteMapping(path = "/{database}/{identification}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> eliminarPersona(@PathVariable String database, @PathVariable Integer identification) {
		try {
			return ResponseEntity.ok(personaInputAdapterRest.eliminarPersona(database.toUpperCase(), identification));
		} catch (InvalidOptionException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (NoExistException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
