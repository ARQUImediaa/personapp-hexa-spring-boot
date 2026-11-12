package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.mapper.EstudioMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterCli {

	@Autowired
	@Qualifier("studyOutputAdapterMaria")
	private StudyOutputPort studyOutputPortMaria;

	@Autowired
	@Qualifier("studyOutputAdapterMongo")
	private StudyOutputPort studyOutputPortMongo;

	@Autowired
	private EstudioMapperCli estudioMapperCli;

	private StudyInputPort studyInputPort;

	public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial Estudio in Input Adapter CLI");
		List<EstudioModelCli> estudios = studyInputPort.findAll().stream()
				.map(estudioMapperCli::fromDomainToAdapterCli).collect(Collectors.toList());
		estudios.forEach(System.out::println);
	}

	public void buscar(Integer idPersona, Integer idProfesion) throws NoExistException {
		Study estudio = studyInputPort.findOne(idPersona, idProfesion);
		System.out.println(estudioMapperCli.fromDomainToAdapterCli(estudio));
	}

	public void crear(Study estudio) {
		// Evita que una pareja persona-profesión repetida sobrescriba el estudio existente
		if (existe(estudio.getPerson().getIdentification(), estudio.getProfession().getIdentification())) {
			System.out.println("Ya existe un estudio de esa persona en esa profesión");
			return;
		}
		Study creado = studyInputPort.create(estudio);
		System.out.println("Estudio creado: " + estudioMapperCli.fromDomainToAdapterCli(creado));
	}

	public void editar(Integer idPersona, Integer idProfesion, Study estudio) throws NoExistException {
		Study editado = studyInputPort.edit(idPersona, idProfesion, estudio);
		System.out.println("Estudio actualizado: " + estudioMapperCli.fromDomainToAdapterCli(editado));
	}

	public void eliminar(Integer idPersona, Integer idProfesion) throws NoExistException {
		Boolean eliminado = studyInputPort.drop(idPersona, idProfesion);
		System.out.println(eliminado ? "Estudio eliminado" : "No se pudo eliminar el estudio");
	}

	public void contar() {
		System.out.println("Total de estudios: " + studyInputPort.count());
	}

	private boolean existe(Integer idPersona, Integer idProfesion) {
		try {
			studyInputPort.findOne(idPersona, idProfesion);
			return true;
		} catch (NoExistException e) {
			return false;
		}
	}
}
