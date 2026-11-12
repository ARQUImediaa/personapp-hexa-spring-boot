package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.mapper.TelefonoMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterCli {

	@Autowired
	@Qualifier("phoneOutputAdapterMaria")
	private PhoneOutputPort phoneOutputPortMaria;

	@Autowired
	@Qualifier("phoneOutputAdapterMongo")
	private PhoneOutputPort phoneOutputPortMongo;

	@Autowired
	private TelefonoMapperCli telefonoMapperCli;

	private PhoneInputPort phoneInputPort;

	public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
		log.info("Into historial Telefono in Input Adapter CLI");
		List<TelefonoModelCli> telefonos = phoneInputPort.findAll().stream()
				.map(telefonoMapperCli::fromDomainToAdapterCli).collect(Collectors.toList());
		telefonos.forEach(System.out::println);
	}

	public void buscar(String numero) throws NoExistException {
		Phone telefono = phoneInputPort.findOne(numero);
		System.out.println(telefonoMapperCli.fromDomainToAdapterCli(telefono));
	}

	public void crear(Phone telefono) {
		// Evita que un número repetido sobrescriba el teléfono existente
		if (existe(telefono.getNumber())) {
			System.out.println("Ya existe un teléfono con el número " + telefono.getNumber());
			return;
		}
		Phone creado = phoneInputPort.create(telefono);
		System.out.println("Teléfono creado: " + telefonoMapperCli.fromDomainToAdapterCli(creado));
	}

	public void editar(String numero, Phone telefono) throws NoExistException {
		Phone editado = phoneInputPort.edit(numero, telefono);
		System.out.println("Teléfono actualizado: " + telefonoMapperCli.fromDomainToAdapterCli(editado));
	}

	public void eliminar(String numero) throws NoExistException {
		Boolean eliminado = phoneInputPort.drop(numero);
		System.out.println(eliminado ? "Teléfono eliminado" : "No se pudo eliminar el teléfono");
	}

	public void contar() {
		System.out.println("Total de teléfonos: " + phoneInputPort.count());
	}

	private boolean existe(String numero) {
		try {
			phoneInputPort.findOne(numero);
			return true;
		} catch (NoExistException e) {
			return false;
		}
	}
}
