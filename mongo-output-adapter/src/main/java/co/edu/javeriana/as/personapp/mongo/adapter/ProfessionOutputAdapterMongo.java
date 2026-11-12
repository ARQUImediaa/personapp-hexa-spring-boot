package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;

@Adapter("professionOutputAdapterMongo")
public class ProfessionOutputAdapterMongo implements ProfessionOutputPort {

	@Autowired
	private ProfesionRepositoryMongo profesionRepositoryMongo;

	@Autowired
	private EstudiosRepositoryMongo estudiosRepositoryMongo;

	@Autowired
	private ProfesionMapperMongo profesionMapperMongo;

	@Override
	public Profession save(Profession profession) {
		return profesionMapperMongo.fromAdapterToDomain(
				profesionRepositoryMongo.save(profesionMapperMongo.fromDomainToAdapter(profession)));
	}

	// Mongo no tiene llaves foráneas: al borrar una profesión se borran sus estudios
	@Override
	public Boolean delete(Integer identification) {
		estudiosRepositoryMongo.findAll().stream()
				.filter(estudio -> estudio.getPrimaryProfesion() != null
						&& identification.equals(estudio.getPrimaryProfesion().getId()))
				.forEach(estudio -> estudiosRepositoryMongo.deleteById(estudio.getId()));
		profesionRepositoryMongo.deleteById(identification);
		return !profesionRepositoryMongo.existsById(identification);
	}

	@Override
	public List<Profession> find() {
		return profesionRepositoryMongo.findAll().stream().map(profesionMapperMongo::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Profession findById(Integer identification) {
		return profesionRepositoryMongo.findById(identification).map(profesionMapperMongo::fromAdapterToDomain)
				.orElse(null);
	}
}
