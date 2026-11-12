package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.adapter.PersonaInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonaMenu {

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

	public void iniciarMenu(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
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
					personaInputAdapterCli.setPersonOutputPortInjection("MARIA");
					menuOpciones(personaInputAdapterCli, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					personaInputAdapterCli.setPersonOutputPortInjection("MONGO");
					menuOpciones(personaInputAdapterCli, keyboard);
					break;
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(PersonaInputAdapterCli personaInputAdapterCli, Scanner keyboard) {
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
					personaInputAdapterCli.historial();
					break;
				case OPCION_BUSCAR:
					personaInputAdapterCli.buscar(leerEntero(keyboard, "DNI de la persona: "));
					break;
				case OPCION_CREAR:
					personaInputAdapterCli.crear(leerPersona(keyboard, null));
					break;
				case OPCION_EDITAR: {
					int dni = leerEntero(keyboard, "DNI de la persona a editar: ");
					personaInputAdapterCli.editar(dni, leerPersona(keyboard, dni));
					break;
				}
				case OPCION_ELIMINAR:
					personaInputAdapterCli.eliminar(leerEntero(keyboard, "DNI de la persona a eliminar: "));
					break;
				case OPCION_CONTAR:
					personaInputAdapterCli.contar();
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
		System.out.println(OPCION_VER_TODO + " para ver todas las personas");
		System.out.println(OPCION_BUSCAR + " para buscar una persona por DNI");
		System.out.println(OPCION_CREAR + " para crear una persona");
		System.out.println(OPCION_EDITAR + " para editar una persona");
		System.out.println(OPCION_ELIMINAR + " para eliminar una persona");
		System.out.println(OPCION_CONTAR + " para contar las personas");
		System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
	}

	private void mostrarMenuMotorPersistencia() {
		System.out.println("----------------------");
		System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
		System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
		System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
	}

	// Pide los datos de una persona. Si dniFijo viene con valor, no vuelve a pedir el DNI
	private Person leerPersona(Scanner keyboard, Integer dniFijo) {
		int dni = dniFijo != null ? dniFijo : leerEntero(keyboard, "DNI: ");
		String nombre = leerTexto(keyboard, "Nombre: ");
		String apellido = leerTexto(keyboard, "Apellido: ");
		Gender genero = leerGenero(keyboard);
		Person person = new Person(dni, nombre, apellido, genero);
		person.setAge(leerEdadOpcional(keyboard));
		return person;
	}

	private Gender leerGenero(Scanner keyboard) {
		while (true) {
			String texto = leerTexto(keyboard, "Género (M, F u O para otro): ");
			switch (texto.toUpperCase()) {
			case "M":
				return Gender.MALE;
			case "F":
				return Gender.FEMALE;
			case "O":
				return Gender.OTHER;
			default:
				log.warn("Escriba M, F u O.");
			}
		}
	}

	private Integer leerEdadOpcional(Scanner keyboard) {
		while (true) {
			String texto = leerTexto(keyboard, "Edad (vacío si no se sabe): ");
			if (texto.isEmpty())
				return null;
			try {
				return Integer.parseInt(texto);
			} catch (NumberFormatException e) {
				log.warn("La edad debe ser un número entero.");
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
