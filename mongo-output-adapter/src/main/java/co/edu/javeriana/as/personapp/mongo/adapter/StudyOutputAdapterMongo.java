package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;

@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {

	@Autowired
	private EstudiosRepositoryMongo estudiosRepositoryMongo;

	@Autowired
	private EstudiosMapperMongo estudiosMapperMongo;

	@Override
	public Study save(Study study) {
		return estudiosMapperMongo.fromAdapterToDomain(
				estudiosRepositoryMongo.save(estudiosMapperMongo.fromDomainToAdapter(study)));
	}

	// El id del documento es "idPersona-idProfesion" (ver EstudiosMapperMongo.validateId)
	@Override
	public Boolean delete(Integer idPerson, Integer idProfession) {
		String id = idPerson + "-" + idProfession;
		estudiosRepositoryMongo.deleteById(id);
		return !estudiosRepositoryMongo.existsById(id);
	}

	@Override
	public List<Study> find() {
		return estudiosRepositoryMongo.findAll().stream().map(estudiosMapperMongo::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Study findById(Integer idPerson, Integer idProfession) {
		return estudiosRepositoryMongo.findById(idPerson + "-" + idProfession)
				.map(estudiosMapperMongo::fromAdapterToDomain).orElse(null);
	}
}
