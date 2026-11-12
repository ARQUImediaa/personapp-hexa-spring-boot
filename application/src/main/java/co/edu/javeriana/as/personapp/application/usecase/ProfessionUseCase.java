package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;

// Service: no es bean de Spring; el adaptador de entrada lo crea con el repositorio elegido (Maria o Mongo)
public class ProfessionUseCase implements ProfessionInputPort {

	private final ProfessionOutputPort professionPersistence;

	public ProfessionUseCase(ProfessionOutputPort professionPersistence) {
		this.professionPersistence = professionPersistence;
	}

	@Override
	public Profession create(Profession profession) {
		return professionPersistence.save(profession);
	}

	@Override
	public Profession edit(Integer identification, Profession profession) throws NoExistException {
		findOne(identification);
		profession.setIdentification(identification);
		return professionPersistence.save(profession);
	}

	@Override
	public Boolean drop(Integer identification) throws NoExistException {
		findOne(identification);
		return professionPersistence.delete(identification);
	}

	@Override
	public List<Profession> findAll() {
		return professionPersistence.find();
	}

	@Override
	public Profession findOne(Integer identification) throws NoExistException {
		Profession profession = professionPersistence.findById(identification);
		if (profession == null)
			throw new NoExistException("La profesión con id " + identification + " no existe");
		return profession;
	}

	@Override
	public Integer count() {
		return findAll().size();
	}
}
