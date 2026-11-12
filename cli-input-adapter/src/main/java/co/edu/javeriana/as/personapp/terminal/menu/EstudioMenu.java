package co.edu.javeriana.as.personapp.terminal.menu;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.adapter.EstudioInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstudioMenu {

	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_BUSCAR = 2;
	private static final int OPCION_CREAR = 3;
	private static final int OPCION_EDITAR = 4;
	private static final int OPCION_ELIMINAR = 5;
	private static final int OPCION_CONTAR = 6;

	public void iniciarMenu(EstudioInputAdapterCli adapter, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuMotorPersistencia();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR_MODULOS:
					isValid = true;
					break;
				case PERSISTENCIA_MARIADB:
					adapter.setStudyOutputPortInjection("MARIA");
					menuOpciones(adapter, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					adapter.setStudyOutputPortInjection("MONGO");
					menuOpciones(adapter, keyboard);
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(EstudioInputAdapterCli adapter, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuOpciones();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
					isValid = true;
					break;
				case OPCION_VER_TODO:
					adapter.historial();
					break;
				case OPCION_BUSCAR: {
					int idPersona = leerEntero(keyboard, "DNI de la persona: ");
					adapter.buscar(idPersona, leerEntero(keyboard, "Id de la profesión: "));
					break;
				}
				case OPCION_CREAR:
					adapter.crear(leerEstudio(keyboard, null, null));
					break;
				case OPCION_EDITAR: {
					int idPersona = leerEntero(keyboard, "DNI de la persona del estudio a editar: ");
					int idProfesion = leerEntero(keyboard, "Id de la profesión del estudio a editar: ");
					adapter.editar(idPersona, idProfesion, leerEstudio(keyboard, idPersona, idProfesion));
					break;
				}
				case OPCION_ELIMINAR: {
					int idPersona = leerEntero(keyboard, "DNI de la persona del estudio a eliminar: ");
					adapter.eliminar(idPersona, leerEntero(keyboard, "Id de la profesión del estudio a eliminar: "));
					break;
				}
				case OPCION_CONTAR:
					adapter.contar();
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (NoExistException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todos los estudios");
		System.out.println(OPCION_BUSCAR + " para buscar un estudio (persona y profesión)");
		System.out.println(OPCION_CREAR + " para crear un estudio");
		System.out.println(OPCION_EDITAR + " para editar un estudio");
		System.out.println(OPCION_ELIMINAR + " para eliminar un estudio");
		System.out.println(OPCION_CONTAR + " para contar los estudios");
		System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
	}

	private void mostrarMenuMotorPersistencia() {
		System.out.println("----------------------");
		System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
		System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
		System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
	}

	// Pide los datos de un estudio. Si idPersonaFijo/idProfesionFijo vienen con valor, no los vuelve a pedir
	private Study leerEstudio(Scanner keyboard, Integer idPersonaFijo, Integer idProfesionFijo) {
		int idPersona = idPersonaFijo != null ? idPersonaFijo : leerEntero(keyboard, "DNI de la persona: ");
		int idProfesion = idProfesionFijo != null ? idProfesionFijo : leerEntero(keyboard, "Id de la profesión: ");
		LocalDate fecha = leerFechaOpcional(keyboard);
		String universidad = leerTexto(keyboard, "Universidad (vacío si no aplica): ");
		Study estudio = new Study(
				new Person(idPersona, "", "", Gender.OTHER),
				new Profession(idProfesion, ""));
		estudio.setGraduationDate(fecha);
		estudio.setUniversityName(universidad);
		return estudio;
	}

	private LocalDate leerFechaOpcional(Scanner keyboard) {
		while (true) {
			String texto = leerTexto(keyboard, "Fecha de graduación yyyy-MM-dd (vacío si no se sabe): ");
			if (texto.isEmpty())
				return null;
			try {
				return LocalDate.parse(texto);
			} catch (DateTimeParseException e) {
				log.warn("La fecha debe tener el formato yyyy-MM-dd.");
			}
		}
	}

	private int leerEntero(Scanner keyboard, String mensaje) {
		while (true) {
			try {
				return Integer.parseInt(leerTexto(keyboard, mensaje));
			} catch (NumberFormatException e) {
				log.warn("Debe ser un número entero.");
			}
		}
	}

	private String leerTexto(Scanner keyboard, String mensaje) {
		System.out.print(mensaje);
		return keyboard.nextLine().trim();
	}

	// Se lee línea completa (no nextInt) para no dejar saltos de línea pendientes en el teclado
	private int leerOpcion(Scanner keyboard) {
		while (true) {
			try {
				return Integer.parseInt(leerTexto(keyboard, "Ingrese una opción: "));
			} catch (NumberFormatException e) {
				log.warn("Solo se permiten números.");
			}
		}
	}
}
