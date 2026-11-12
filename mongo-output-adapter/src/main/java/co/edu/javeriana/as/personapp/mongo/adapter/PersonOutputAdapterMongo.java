package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoWriteException;

import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.PersonaMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.PersonaRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.TelefonoRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("personOutputAdapterMongo")
public class PersonOutputAdapterMongo implements PersonOutputPort {

	@Autowired
	private PersonaRepositoryMongo personaRepositoryMongo;

	@Autowired
	private TelefonoRepositoryMongo telefonoRepositoryMongo;

	@Autowired
	private EstudiosRepositoryMongo estudiosRepositoryMongo;

	@Autowired
	private PersonaMapperMongo personaMapperMongo;

	@Override
	public Person save(Person person) {
		log.debug("Into save on Adapter MongoDB");
		try {
			PersonaDocument persistedPersona = personaRepositoryMongo.save(personaMapperMongo.fromDomainToAdapter(person));
			return personaMapperMongo.fromAdapterToDomain(persistedPersona);
		} catch (MongoWriteException e) {
			log.warn(e.getMessage());
			return person;
		}
	}

	// Mongo no tiene llaves foráneas: al borrar una persona se borran sus teléfonos y estudios
	@Override
	public Boolean delete(Integer identification) {
		log.debug("Into delete on Adapter MongoDB");
		telefonoRepositoryMongo.findAll().stream()
				.filter(telefono -> telefono.getPrimaryDuenio() != null
						&& identification.equals(telefono.getPrimaryDuenio().getId()))
				.forEach(telefono -> telefonoRepositoryMongo.deleteById(telefono.getId()));
		estudiosRepositoryMongo.findAll().stream()
				.filter(estudio -> estudio.getPrimaryPersona() != null
						&& identification.equals(estudio.getPrimaryPersona().getId()))
				.forEach(estudio -> estudiosRepositoryMongo.deleteById(estudio.getId()));
		personaRepositoryMongo.deleteById(identification);
		return personaRepositoryMongo.findById(identification).isEmpty();
	}

	@Override
	public List<Person> find() {
		log.debug("Into find on Adapter MongoDB");
		return personaRepositoryMongo.findAll().stream().map(personaMapperMongo::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Person findById(Integer identification) {
		log.debug("Into findById on Adapter MongoDB");
		if (personaRepositoryMongo.findById(identification).isEmpty()) {
			return null;
		} else {
			return personaMapperMongo.fromAdapterToDomain(personaRepositoryMongo.findById(identification).get());
		}
	}

}
