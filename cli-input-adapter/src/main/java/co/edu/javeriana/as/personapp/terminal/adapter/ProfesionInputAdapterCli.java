package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {

	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private ProfesionMapperCli profesionMapperCli;

	private ProfessionInputPort professionInputPort;

	public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial Profesion in Input Adapter CLI");
		List<ProfesionModelCli> profesiones = professionInputPort.findAll().stream()
				.map(profesionMapperCli::fromDomainToAdapterCli).collect(Collectors.toList());
		profesiones.forEach(System.out::println);
	}

	public void buscar(Integer id) throws NoExistException {
		Profession profesion = professionInputPort.findOne(id);
		System.out.println(profesionMapperCli.fromDomainToAdapterCli(profesion));
	}

	public void crear(Profession profesion) {
		// Evita que un id repetido sobrescriba la profesión existente
		if (existe(profesion.getIdentification())) {
			System.out.println("Ya existe una profesión con el id " + profesion.getIdentification());
			return;
		}
		Profession creada = professionInputPort.create(profesion);
		System.out.println("Profesión creada: " + profesionMapperCli.fromDomainToAdapterCli(creada));
	}

	public void editar(Integer id, Profession profesion) throws NoExistException {
		Profession editada = professionInputPort.edit(id, profesion);
		System.out.println("Profesión actualizada: " + profesionMapperCli.fromDomainToAdapterCli(editada));
	}

	public void eliminar(Integer id) throws NoExistException {
		Boolean eliminada = professionInputPort.drop(id);
		System.out.println(eliminada ? "Profesión eliminada" : "No se pudo eliminar la profesión");
	}

	public void contar() {
		System.out.println("Total de profesiones: " + professionInputPort.count());
	}

	private boolean existe(Integer id) {
		try {
			professionInputPort.findOne(id);
			return true;
		} catch (NoExistException e) {
			return false;
		}
	}
}
