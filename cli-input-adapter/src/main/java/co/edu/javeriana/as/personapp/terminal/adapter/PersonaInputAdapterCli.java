package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial1() {
		log.info("Into historial PersonaEntity in Input Adapter");
		List<PersonaModelCli> persona = personInputPort.findAll().stream().map(personaMapperCli::fromDomainToAdapterCli)
					.collect(Collectors.toList());
		persona.forEach(p -> System.out.println(p.toString()));
	}

	public void historial() {
	    log.info("Into historial PersonaEntity in Input Adapter");
	    personInputPort.findAll().stream()
	        .map(personaMapperCli::fromDomainToAdapterCli)
	        .forEach(System.out::println);
	}

	public void buscar(Integer cc) throws NoExistException {
		Person person = personInputPort.findOne(cc);
		System.out.println(personaMapperCli.fromDomainToAdapterCli(person));
	}

	public void crear(Person person) throws NoExistException {
		// Evita que un DNI repetido sobrescriba a la persona existente
		if (existe(person.getIdentification())) {
			System.out.println("Ya existe una persona con el DNI " + person.getIdentification());
			return;
		}
		Person creada = personInputPort.create(person);
		System.out.println("Persona creada: " + personaMapperCli.fromDomainToAdapterCli(creada));
	}

	public void editar(Integer cc, Person person) throws NoExistException {
		Person editada = personInputPort.edit(cc, person);
		System.out.println("Persona actualizada: " + personaMapperCli.fromDomainToAdapterCli(editada));
	}

	public void eliminar(Integer cc) throws NoExistException {
		Boolean eliminada = personInputPort.drop(cc);
		System.out.println(eliminada ? "Persona eliminada" : "No se pudo eliminar la persona");
	}

	public void contar() {
		System.out.println("Total de personas: " + personInputPort.count());
	}

	private boolean existe(Integer cc) {
		try {
			personInputPort.findOne(cc);
			return true;
		} catch (NoExistException e) {
			return false;
		}
	}
}
