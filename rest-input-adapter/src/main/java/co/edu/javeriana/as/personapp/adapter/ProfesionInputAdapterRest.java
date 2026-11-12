package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfesionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;

@Adapter
public class ProfesionInputAdapterRest {

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private ProfesionMapperRest profesionMapperRest;

	private ProfessionInputPort professionInputPort;

	private String setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		}
		throw new InvalidOptionException("Opción de base de datos inválida: " + dbOption);
	}

	public List<ProfesionResponse> historial(String database) throws InvalidOptionException {
		String db = setProfessionOutputPortInjection(database);
		return professionInputPort.findAll().stream()
				.map(profesion -> profesionMapperRest.fromDomainToAdapterRest(profesion, db))
				.collect(Collectors.toList());
	}

	public Integer contar(String database) throws InvalidOptionException {
		setProfessionOutputPortInjection(database);
		return professionInputPort.count();
	}

	public ProfesionResponse buscar(String database, Integer identification)
			throws InvalidOptionException, NoExistException {
		String db = setProfessionOutputPortInjection(database);
		return profesionMapperRest.fromDomainToAdapterRest(professionInputPort.findOne(identification), db);
	}

	public ProfesionResponse crear(String database, ProfesionRequest request)
			throws InvalidOptionException, AlreadyExistException {
		String db = setProfessionOutputPortInjection(database);
		Profession nueva = profesionMapperRest.fromAdapterToDomain(request);
		if (existe(nueva.getIdentification()))
			throw new AlreadyExistException("Ya existe una profesión con el id " + nueva.getIdentification());
		return profesionMapperRest.fromDomainToAdapterRest(professionInputPort.create(nueva), db);
	}

	// PUT parcial: nombre y descripción que no vienen en el request conservan su valor actual
	public ProfesionResponse editar(String database, Integer identification, ProfesionRequest request)
			throws InvalidOptionException, NoExistException {
		String db = setProfessionOutputPortInjection(database);
		Profession actual = professionInputPort.findOne(identification);
		if (request.getNombre() == null)
			request.setNombre(actual.getName());
		if (request.getDescripcion() == null)
			request.setDescripcion(actual.getDescription());
		request.setId(String.valueOf(identification));
		return profesionMapperRest.fromDomainToAdapterRest(
				professionInputPort.edit(identification, profesionMapperRest.fromAdapterToDomain(request)), db);
	}

	public Boolean eliminar(String database, Integer identification)
			throws InvalidOptionException, NoExistException {
		setProfessionOutputPortInjection(database);
		return professionInputPort.drop(identification);
	}

	private boolean existe(Integer identification) {
		try {
			professionInputPort.findOne(identification);
			return true;
		} catch (NoExistException e) {
			return false;
		}
	}
}
