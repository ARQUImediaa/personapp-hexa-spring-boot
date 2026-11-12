package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepositoryMaria;

@Adapter("studyOutputAdapterMaria")
@Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {

	@Autowired
	private EstudiosRepositoryMaria estudiosRepositoryMaria;

	@Autowired
	private EstudiosMapperMaria estudiosMapperMaria;

	@Override
	public Study save(Study study) {
		return estudiosMapperMaria.fromAdapterToDomain(
				estudiosRepositoryMaria.save(estudiosMapperMaria.fromDomainToAdapter(study)));
	}

	@Override
	public Boolean delete(Integer idPerson, Integer idProfession) {
		EstudiosEntityPK pk = new EstudiosEntityPK(idProfession, idPerson);
		estudiosRepositoryMaria.deleteById(pk);
		return !estudiosRepositoryMaria.existsById(pk);
	}

	@Override
	public List<Study> find() {
		return estudiosRepositoryMaria.findAll().stream().map(estudiosMapperMaria::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Study findById(Integer idPerson, Integer idProfession) {
		return estudiosRepositoryMaria.findById(new EstudiosEntityPK(idProfession, idPerson))
				.map(estudiosMapperMaria::fromAdapterToDomain).orElse(null);
	}
}
