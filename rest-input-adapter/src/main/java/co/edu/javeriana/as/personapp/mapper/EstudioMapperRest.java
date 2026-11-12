package co.edu.javeriana.as.personapp.mapper;

import java.time.LocalDate;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;

@Mapper
public class EstudioMapperRest {

	public EstudioResponse fromDomainToAdapterRest(Study study, String database) {
		return new EstudioResponse(
				String.valueOf(study.getProfession().getIdentification()),
				String.valueOf(study.getPerson().getIdentification()),
				study.getGraduationDate() != null ? study.getGraduationDate().toString() : "",
				study.getUniversityName(),
				database,
				"OK");
	}

	public Study fromAdapterToDomain(EstudioRequest request) {
		Person person = new Person(Integer.parseInt(request.getDniPersona()), "", "", Gender.OTHER);
		Profession profession = new Profession(Integer.parseInt(request.getIdProfesion()), "");
		Study study = new Study(person, profession);
		study.setGraduationDate(
				request.getFechaGraduacion() != null && !request.getFechaGraduacion().isBlank()
						? LocalDate.parse(request.getFechaGraduacion())
						: null);
		study.setUniversityName(request.getUniversidad());
		return study;
	}
}
