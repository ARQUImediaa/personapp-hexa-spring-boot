package co.edu.javeriana.as.personapp.mapper;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

@Mapper
public class PersonaMapperRest {
	
	public PersonaResponse fromDomainToAdapterRestMaria(Person person) {
		return fromDomainToAdapterRest(person, "MariaDB");
	}
	public PersonaResponse fromDomainToAdapterRestMongo(Person person) {
		return fromDomainToAdapterRest(person, "MongoDB");
	}
	
	// Versión de las consultas: incluye teléfonos y estudios (con su profesión)
	public PersonaResponse fromDomainToAdapterRestDetalle(Person person, String database) {
		PersonaResponse response = fromDomainToAdapterRest(person, database);
		List<PersonaResponse.TelefonoDetalle> telefonos = new ArrayList<>();
		if (person.getPhoneNumbers() != null)
			person.getPhoneNumbers().forEach(
					phone -> telefonos.add(new PersonaResponse.TelefonoDetalle(phone.getNumber(), phone.getCompany())));
		List<PersonaResponse.EstudioDetalle> estudios = new ArrayList<>();
		if (person.getStudies() != null)
			person.getStudies().forEach(study -> estudios.add(new PersonaResponse.EstudioDetalle(
					String.valueOf(study.getProfession().getIdentification()),
					study.getProfession().getName(),
					study.getProfession().getDescription(),
					study.getGraduationDate() != null ? study.getGraduationDate().toString() : null,
					study.getUniversityName())));
		response.setTelefonos(telefonos);
		response.setEstudios(estudios);
		return response;
	}

	public PersonaResponse fromDomainToAdapterRest(Person person, String database) {
		return new PersonaResponse(
				person.getIdentification()+"", 
				person.getFirstName(), 
				person.getLastName(), 
				person.getAge()+"", 
				person.getGender().toString(), 
				database,
				"OK");
	}

	public Person fromAdapterToDomain(PersonaRequest request) {
		Person person = new Person(
				Integer.parseInt(request.getDni()),
				request.getFirstName(),
				request.getLastName(),
				toGender(request.getSex()));
		if (request.getAge() != null && !request.getAge().isBlank())
			person.setAge(Integer.parseInt(request.getAge()));
		return person;
	}

	private Gender toGender(String sex) {
		if (sex == null)
			return Gender.OTHER;
		switch (sex.trim().toUpperCase()) {
		case "M":
		case "MALE":
			return Gender.MALE;
		case "F":
		case "FEMALE":
			return Gender.FEMALE;
		default:
			return Gender.OTHER;
		}
	}
		
}
