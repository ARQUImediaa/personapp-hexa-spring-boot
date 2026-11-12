package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;

// Un estudio se identifica por la pareja (persona, profesión)
@Port
public interface StudyInputPort {

	public Study create(Study study);
	public Study edit(Integer idPerson, Integer idProfession, Study study) throws NoExistException;
	public Boolean drop(Integer idPerson, Integer idProfession) throws NoExistException;
	public List<Study> findAll();
	public Study findOne(Integer idPerson, Integer idProfession) throws NoExistException;
	public Integer count();
}
