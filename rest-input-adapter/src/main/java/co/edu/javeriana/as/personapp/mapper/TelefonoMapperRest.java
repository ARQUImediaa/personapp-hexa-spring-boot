package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;

@Mapper
public class TelefonoMapperRest {

	public TelefonoResponse fromDomainToAdapterRest(Phone phone, String database) {
		return new TelefonoResponse(
				phone.getNumber(),
				phone.getCompany(),
				String.valueOf(phone.getOwner().getIdentification()),
				database,
				"OK");
	}

	// El dueño solo necesita su documento: el resto de datos lo tiene la tabla persona
	public Phone fromAdapterToDomain(TelefonoRequest request) {
		Person owner = new Person(Integer.parseInt(request.getDniDuenio()), "", "", Gender.OTHER);
		return new Phone(request.getNumero(), request.getOperador(), owner);
	}
}
