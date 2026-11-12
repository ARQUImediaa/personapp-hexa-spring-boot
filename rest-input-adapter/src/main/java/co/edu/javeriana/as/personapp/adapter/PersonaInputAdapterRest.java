package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterRest {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperRest personaMapperRest;

	PersonInputPort personInputPort;

	private String setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<PersonaResponse> historial(String database) throws InvalidOptionException {
		String db = setPersonOutputPortInjection(database);
		return personInputPort.findAll().stream().map(persona -> mapearRespuesta(db, persona))
				.collect(Collectors.toList());
	}

	public PersonaResponse crearPersona(PersonaRequest request)
			throws InvalidOptionException, AlreadyExistException {
		String db = setPersonOutputPortInjection(request.getDatabase());
		Person nueva = personaMapperRest.fromAdapterToDomain(request);
		if (existe(nueva.getIdentification()))
			throw new AlreadyExistException("Ya existe una persona con el DNI " + nueva.getIdentification());
		return mapearRespuesta(db, personInputPort.create(nueva));
	}

	public PersonaResponse buscarPersona(String database, Integer identification)
			throws InvalidOptionException, NoExistException {
		String db = setPersonOutputPortInjection(database);
		return mapearRespuesta(db, personInputPort.findOne(identification));
	}

	// PUT parcial: los campos que no vienen en el request conservan su valor actual
	public PersonaResponse editarPersona(String database, Integer identification, PersonaRequest request)
			throws InvalidOptionException, NoExistException {
		String db = setPersonOutputPortInjection(database);
		Person actual = personInputPort.findOne(identification);
		if (request.getFirstName() == null)
			request.setFirstName(actual.getFirstName());
		if (request.getLastName() == null)
			request.setLastName(actual.getLastName());
		if (request.getSex() == null)
			request.setSex(actual.getGender().name());
		if (request.getAge() == null && actual.getAge() != null)
			request.setAge(String.valueOf(actual.getAge()));
		request.setDni(String.valueOf(identification));
		Person person = personaMapperRest.fromAdapterToDomain(request);
		return mapearRespuesta(db, personInputPort.edit(identification, person));
	}

	public Boolean eliminarPersona(String database, Integer identification)
			throws InvalidOptionException, NoExistException {
		setPersonOutputPortInjection(database);
		return personInputPort.drop(identification);
	}

	public Integer contarPersonas(String database) throws InvalidOptionException {
		setPersonOutputPortInjection(database);
		return personInputPort.count();
	}

	private boolean existe(Integer identification) {
		try {
			personInputPort.findOne(identification);
			return true;
		} catch (NoExistException e) {
			return false;
		}
	}

	// La etiqueta de la respuesta indica la base donde realmente quedó guardada la persona
	private PersonaResponse mapearRespuesta(String database, Person person) {
		if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString()))
			return personaMapperRest.fromDomainToAdapterRestMaria(person);
		return personaMapperRest.fromDomainToAdapterRestMongo(person);
	}
}
