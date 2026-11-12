package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;

// Service: no es bean de Spring; el adaptador de entrada lo crea con el repositorio elegido (Maria o Mongo)
public class PhoneUseCase implements PhoneInputPort {

	private final PhoneOutputPort phonePersistence;

	public PhoneUseCase(PhoneOutputPort phonePersistence) {
		this.phonePersistence = phonePersistence;
	}

	@Override
	public Phone create(Phone phone) {
		return phonePersistence.save(phone);
	}

	@Override
	public Phone edit(String number, Phone phone) throws NoExistException {
		findOne(number);
		phone.setNumber(number);
		return phonePersistence.save(phone);
	}

	@Override
	public Boolean drop(String number) throws NoExistException {
		findOne(number);
		return phonePersistence.delete(number);
	}

	@Override
	public List<Phone> findAll() {
		return phonePersistence.find();
	}

	@Override
	public Phone findOne(String number) throws NoExistException {
		Phone phone = phonePersistence.findById(number);
		if (phone == null)
			throw new NoExistException("El teléfono " + number + " no existe");
		return phone;
	}

	@Override
	public Integer count() {
		return findAll().size();
	}
}
