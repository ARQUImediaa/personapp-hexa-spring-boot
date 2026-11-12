package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;

@Adapter("professionOutputAdapterMaria")
@Transactional
public class ProfessionOutputAdapterMaria implements ProfessionOutputPort {

	@Autowired
	private ProfesionRepositoryMaria profesionRepositoryMaria;

	@Autowired
	private ProfesionMapperMaria profesionMapperMaria;

	@Override
	public Profession save(Profession profession) {
		return profesionMapperMaria.fromAdapterToDomain(
				profesionRepositoryMaria.save(profesionMapperMaria.fromDomainToAdapter(profession)));
	}

	@Override
	public Boolean delete(Integer identification) {
		profesionRepositoryMaria.deleteById(identification);
		return !profesionRepositoryMaria.existsById(identification);
	}

	@Override
	public List<Profession> find() {
		return profesionRepositoryMaria.findAll().stream().map(profesionMapperMaria::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Profession findById(Integer identification) {
		return profesionRepositoryMaria.findById(identification).map(profesionMapperMaria::fromAdapterToDomain)
				.orElse(null);
	}
}
