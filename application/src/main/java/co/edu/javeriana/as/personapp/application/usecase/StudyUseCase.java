package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;

// Service: no es bean de Spring; el adaptador de entrada lo crea con el repositorio elegido (Maria o Mongo)
public class StudyUseCase implements StudyInputPort {

	private final StudyOutputPort studyPersistence;

	public StudyUseCase(StudyOutputPort studyPersistence) {
		this.studyPersistence = studyPersistence;
	}

	@Override
	public Study create(Study study) {
		return studyPersistence.save(study);
	}

	@Override
	public Study edit(Integer idPerson, Integer idProfession, Study study) throws NoExistException {
		findOne(idPerson, idProfession);
		study.getPerson().setIdentification(idPerson);
		study.getProfession().setIdentification(idProfession);
		return studyPersistence.save(study);
	}

	@Override
	public Boolean drop(Integer idPerson, Integer idProfession) throws NoExistException {
		findOne(idPerson, idProfession);
		return studyPersistence.delete(idPerson, idProfession);
	}

	@Override
	public List<Study> findAll() {
		return studyPersistence.find();
	}

	@Override
	public Study findOne(Integer idPerson, Integer idProfession) throws NoExistException {
		Study study = studyPersistence.findById(idPerson, idProfession);
		if (study == null)
			throw new NoExistException("El estudio de la persona " + idPerson + " en la profesión " + idProfession + " no existe");
		return study;
	}

	@Override
	public Integer count() {
		return findAll().size();
	}
}
