package co.edu.javeriana.as.personapp.common.exceptions;

// Se lanza al crear un registro cuya llave ya existe en la base de datos
public class AlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AlreadyExistException(String message) {
		super(message);
	}
}
