package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.EstudioMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;

@Adapter
public class EstudioInputAdapterRest {

	@Autowired
	@Qualifier("studyOutputAdapterMaria")
	private StudyOutputPort studyOutputPortMaria;

	@Autowired
	@Qualifier("studyOutputAdapterMongo")
	private StudyOutputPort studyOutputPortMongo;

	@Autowired
	private EstudioMapperRest estudioMapperRest;

	private StudyInputPort studyInputPort;

	private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		}
		throw new InvalidOptionException("Opción de base de datos inválida: " + dbOption);
	}

	public List<EstudioResponse> historial(String database) throws InvalidOptionException {
		String db = setStudyOutputPortInjection(database);
		return studyInputPort.findAll().stream()
				.map(estudio -> estudioMapperRest.fromDomainToAdapterRest(estudio, db))
				.collect(Collectors.toList());
	}

	public Integer contar(String database) throws InvalidOptionException {
		setStudyOutputPortInjection(database);
		return studyInputPort.count();
	}

	public EstudioResponse buscar(String database, Integer idPersona, Integer idProfesion)
			throws InvalidOptionException, NoExistException {
		String db = setStudyOutputPortInjection(database);
		return estudioMapperRest.fromDomainToAdapterRest(studyInputPort.findOne(idPersona, idProfesion), db);
	}

	public EstudioResponse crear(String database, EstudioRequest request)
			throws InvalidOptionException, AlreadyExistException {
		String db = setStudyOutputPortInjection(database);
		Study nuevo = estudioMapperRest.fromAdapterToDomain(request);
		Integer idPersona = nuevo.getPerson().getIdentification();
		Integer idProfesion = nuevo.getProfession().getIdentification();
		if (existe(idPersona, idProfesion))
			throw new AlreadyExistException("Ya existe un estudio de la persona " + idPersona
					+ " en la profesión " + idProfesion);
		return estudioMapperRest.fromDomainToAdapterRest(studyInputPort.create(nuevo), db);
	}

	// PUT parcial: fecha y universidad que no vienen en el request conservan su valor actual
	public EstudioResponse editar(String database, Integer idPersona, Integer idProfesion, EstudioRequest request)
			throws InvalidOptionException, NoExistException {
		String db = setStudyOutputPortInjection(database);
		Study actual = studyInputPort.findOne(idPersona, idProfesion);
		if (request.getFechaGraduacion() == null && actual.getGraduationDate() != null)
			request.setFechaGraduacion(actual.getGraduationDate().toString());
		if (request.getUniversidad() == null)
			request.setUniversidad(actual.getUniversityName());
		request.setDniPersona(String.valueOf(idPersona));
		request.setIdProfesion(String.valueOf(idProfesion));
		return estudioMapperRest.fromDomainToAdapterRest(
				studyInputPort.edit(idPersona, idProfesion, estudioMapperRest.fromAdapterToDomain(request)), db);
	}

	public Boolean eliminar(String database, Integer idPersona, Integer idProfesion)
			throws InvalidOptionException, NoExistException {
		setStudyOutputPortInjection(database);
		return studyInputPort.drop(idPersona, idProfesion);
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
