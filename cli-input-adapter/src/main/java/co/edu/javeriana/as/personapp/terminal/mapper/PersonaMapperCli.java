package co.edu.javeriana.as.personapp.terminal.mapper;

import java.util.ArrayList;
import java.util.List;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;

@Mapper
public class PersonaMapperCli {

	public PersonaModelCli fromDomainToAdapterCli(Person person) {
		PersonaModelCli personaModelCli = new PersonaModelCli();
		personaModelCli.setCc(person.getIdentification());
		personaModelCli.setNombre(person.getFirstName());
		personaModelCli.setApellido(person.getLastName());
		personaModelCli.setGenero(person.getGender().toString());
		personaModelCli.setEdad(person.getAge());
		return personaModelCli;
	}

	// Versión de las consultas: incluye teléfonos y estudios (con su profesión)
	public PersonaModelCli fromDomainToAdapterCliDetalle(Person person) {
		PersonaModelCli model = fromDomainToAdapterCli(person);
		List<String> telefonos = new ArrayList<>();
		if (person.getPhoneNumbers() != null)
			person.getPhoneNumbers().forEach(p -> telefonos.add(p.getNumber() + " (" + p.getCompany() + ")"));
		List<String> estudios = new ArrayList<>();
		if (person.getStudies() != null)
			person.getStudies().forEach(s -> estudios.add(s.getProfession().getName() + " - "
					+ (s.getUniversityName() == null || s.getUniversityName().isEmpty() ? "sin universidad"
							: s.getUniversityName())
					+ (s.getGraduationDate() != null ? " (" + s.getGraduationDate() + ")" : "")));
		model.setTelefonos(telefonos);
		model.setEstudios(estudios);
		return model;
	}
}
