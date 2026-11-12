package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.mapper.TelefonoMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.TelefonoRepositoryMongo;

@Adapter("phoneOutputAdapterMongo")
public class PhoneOutputAdapterMongo implements PhoneOutputPort {

	@Autowired
	private TelefonoRepositoryMongo telefonoRepositoryMongo;

	@Autowired
	private TelefonoMapperMongo telefonoMapperMongo;

	@Override
	public Phone save(Phone phone) {
		return telefonoMapperMongo.fromAdapterToDomain(
				telefonoRepositoryMongo.save(telefonoMapperMongo.fromDomainToAdapter(phone)));
	}

	@Override
	public Boolean delete(String number) {
		telefonoRepositoryMongo.deleteById(number);
		return !telefonoRepositoryMongo.existsById(number);
	}

	@Override
	public List<Phone> find() {
		return telefonoRepositoryMongo.findAll().stream().map(telefonoMapperMongo::fromAdapterToDomain)
				.collect(Collectors.toList());
	}

	@Override
	public Phone findById(String number) {
		return telefonoRepositoryMongo.findById(number).map(telefonoMapperMongo::fromAdapterToDomain).orElse(null);
	}
}
