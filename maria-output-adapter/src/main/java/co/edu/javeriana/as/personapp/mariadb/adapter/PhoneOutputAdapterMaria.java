package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;

@Adapter("phoneOutputAdapterMaria")
@Transactional
public class PhoneOutputAdapterMaria implements PhoneOutputPort {

	@Autowired
	private TelefonoRepositoryMaria telefonoRepositoryMaria;

	@Autowired
	private TelefonoMapperMaria telefonoMapperMaria;

	@Override
	public Phone save(Phone phone) {
		return telefonoMapperMaria.fromAdapterToDomain(
				telefonoRepositoryMaria.save(telefonoMapperMaria.fromDomainToAdapter(phone)));
	}

	@Override
	public Boolean delete(String number) {
		telefonoRepositoryMaria.deleteById(number);
		return !telefonoRepositoryMaria.existsById(number);
	}

	@Override
	public List<Phone> find() {
		return telefonoRepositoryMaria.findAll().stream().map(telefonoMapperMaria::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Phone findById(String number) {
		return telefonoRepositoryMaria.findById(number).map(telefonoMapperMaria::fromAdapterToDomain).orElse(null);
	}
}
