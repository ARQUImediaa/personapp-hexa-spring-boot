package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;

@RestController
@RequestMapping("/api/v1/profesion")
public class ProfesionControllerV1 {

	@Autowired
	private ProfesionInputAdapterRest profesionInputAdapterRest;

	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProfesionResponse> listar(@PathVariable String database) throws InvalidOptionException {
		return profesionInputAdapterRest.historial(database.toUpperCase());
	}

	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer contar(@PathVariable String database) throws InvalidOptionException {
		return profesionInputAdapterRest.contar(database.toUpperCase());
	}

	@GetMapping(path = "/{database}/{identification}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse buscar(@PathVariable String database, @PathVariable Integer identification)
			throws InvalidOptionException, NoExistException {
		return profesionInputAdapterRest.buscar(database.toUpperCase(), identification);
	}

	@PostMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse crear(@PathVariable String database, @RequestBody ProfesionRequest request)
			throws InvalidOptionException {
		return profesionInputAdapterRest.crear(database.toUpperCase(), request);
	}

	@PutMapping(path = "/{database}/{identification}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse editar(@PathVariable String database, @PathVariable Integer identification,
			@RequestBody ProfesionRequest request) throws InvalidOptionException, NoExistException {
		return profesionInputAdapterRest.editar(database.toUpperCase(), identification, request);
	}

	@DeleteMapping(path = "/{database}/{identification}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean eliminar(@PathVariable String database, @PathVariable Integer identification)
			throws InvalidOptionException, NoExistException {
		return profesionInputAdapterRest.eliminar(database.toUpperCase(), identification);
	}
}
