package co.edu.javeriana.as.personapp.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.AlreadyExistException;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;

@Adapter
public class TelefonoInputAdapterRest {

	@Autowired
	@Qualifier("phoneOutputAdapterMaria")
	private PhoneOutputPort phoneOutputPortMaria;

	@Autowired
	@Qualifier("phoneOutputAdapterMongo")
	private PhoneOutputPort phoneOutputPortMongo;

	@Autowired
	private TelefonoMapperRest telefonoMapperRest;

	private PhoneInputPort phoneInputPort;

	private String setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
			return DatabaseOption.MONGO.toString();
		}
		throw new InvalidOptionException("Opción de base de datos inválida: " + dbOption);
	}

	public List<TelefonoResponse> historial(String database) throws InvalidOptionException {
		String db = setPhoneOutputPortInjection(database);
		return phoneInputPort.findAll().stream()
				.map(telefono -> telefonoMapperRest.fromDomainToAdapterRest(telefono, db))
				.collect(Collectors.toList());
	}

	public Integer contar(String database) throws InvalidOptionException {
		setPhoneOutputPortInjection(database);
		return phoneInputPort.count();
	}

	public TelefonoResponse buscar(String database, String number) throws InvalidOptionException, NoExistException {
		String db = setPhoneOutputPortInjection(database);
		return telefonoMapperRest.fromDomainToAdapterRest(phoneInputPort.findOne(number), db);
	}

	public TelefonoResponse crear(String database, TelefonoRequest request)
			throws InvalidOptionException, AlreadyExistException {
		String db = setPhoneOutputPortInjection(database);
		Phone nuevo = telefonoMapperRest.fromAdapterToDomain(request);
		if (existe(nuevo.getNumber()))
			throw new AlreadyExistException("Ya existe un teléfono con el número " + nuevo.getNumber());
		return telefonoMapperRest.fromDomainToAdapterRest(phoneInputPort.create(nuevo), db);
	}

	// PUT parcial: operador y dueño que no vienen en el request conservan su valor actual
	public TelefonoResponse editar(String database, String number, TelefonoRequest request)
			throws InvalidOptionException, NoExistException {
		String db = setPhoneOutputPortInjection(database);
		Phone actual = phoneInputPort.findOne(number);
		if (request.getOperador() == null)
			request.setOperador(actual.getCompany());
		if (request.getDniDuenio() == null)
			request.setDniDuenio(String.valueOf(actual.getOwner().getIdentification()));
		request.setNumero(number);
		return telefonoMapperRest.fromDomainToAdapterRest(
				phoneInputPort.edit(number, telefonoMapperRest.fromAdapterToDomain(request)), db);
	}

	public Boolean eliminar(String database, String number) throws InvalidOptionException, NoExistException {
		setPhoneOutputPortInjection(database);
		return phoneInputPort.drop(number);
	}

	private boolean existe(String number) {
		try {
			phoneInputPort.findOne(number);
			return true;
		} catch (NoExistException e) {
			return false;
		}
	}
}
