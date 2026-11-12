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

import co.edu.javeriana.as.personapp.adapter.EstudioInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;

@RestController
@RequestMapping("/api/v1/estudio")
public class EstudioControllerV1 {

	@Autowired
	private EstudioInputAdapterRest estudioInputAdapterRest;

	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EstudioResponse> listar(@PathVariable String database) throws InvalidOptionException {
		return estudioInputAdapterRest.historial(database.toUpperCase());
	}

	@GetMapping(path = "/{database}/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer contar(@PathVariable String database) throws InvalidOptionException {
		return estudioInputAdapterRest.contar(database.toUpperCase());
	}

	@GetMapping(path = "/{database}/{idPersona}/{idProfesion}", produces = MediaType.APPLICATION_JSON_VALUE)
	public EstudioResponse buscar(@PathVariable String database, @PathVariable Integer idPersona,
			@PathVariable Integer idProfesion) throws InvalidOptionException, NoExistException {
		return estudioInputAdapterRest.buscar(database.toUpperCase(), idPersona, idProfesion);
	}

	@PostMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public EstudioResponse crear(@PathVariable String database, @RequestBody EstudioRequest request)
			throws InvalidOptionException {
		return estudioInputAdapterRest.crear(database.toUpperCase(), request);
	}

	@PutMapping(path = "/{database}/{idPersona}/{idProfesion}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public EstudioResponse editar(@PathVariable String database, @PathVariable Integer idPersona,
			@PathVariable Integer idProfesion, @RequestBody EstudioRequest request)
			throws InvalidOptionException, NoExistException {
		return estudioInputAdapterRest.editar(database.toUpperCase(), idPersona, idProfesion, request);
	}

	@DeleteMapping(path = "/{database}/{idPersona}/{idProfesion}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean eliminar(@PathVariable String database, @PathVariable Integer idPersona,
			@PathVariable Integer idProfesion) throws InvalidOptionException, NoExistException {
		return estudioInputAdapterRest.eliminar(database.toUpperCase(), idPersona, idProfesion);
	}
}
