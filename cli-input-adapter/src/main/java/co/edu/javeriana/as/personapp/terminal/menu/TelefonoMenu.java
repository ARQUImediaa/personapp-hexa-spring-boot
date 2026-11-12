package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelefonoMenu {

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

	public void iniciarMenu(TelefonoInputAdapterCli adapter, Scanner keyboard) {
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
					adapter.setPhoneOutputPortInjection("MARIA");
					menuOpciones(adapter, keyboard);
					break;
				case PERSISTENCIA_MONGODB:
					adapter.setPhoneOutputPortInjection("MONGO");
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

	private void menuOpciones(TelefonoInputAdapterCli adapter, Scanner keyboard) {
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
				case OPCION_BUSCAR:
					adapter.buscar(leerTexto(keyboard, "Número de teléfono: "));
					break;
				case OPCION_CREAR:
					adapter.crear(leerTelefono(keyboard, null));
					break;
				case OPCION_EDITAR: {
					String numero = leerTexto(keyboard, "Número del teléfono a editar: ");
					adapter.editar(numero, leerTelefono(keyboard, numero));
					break;
				}
				case OPCION_ELIMINAR:
					adapter.eliminar(leerTexto(keyboard, "Número del teléfono a eliminar: "));
					break;
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
		System.out.println(OPCION_VER_TODO + " para ver todos los teléfonos");
		System.out.println(OPCION_BUSCAR + " para buscar un teléfono por número");
		System.out.println(OPCION_CREAR + " para crear un teléfono");
		System.out.println(OPCION_EDITAR + " para editar un teléfono");
		System.out.println(OPCION_ELIMINAR + " para eliminar un teléfono");
		System.out.println(OPCION_CONTAR + " para contar los teléfonos");
		System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
	}

	private void mostrarMenuMotorPersistencia() {
		System.out.println("----------------------");
		System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
		System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
		System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
	}

	// Pide los datos de un teléfono. Si numeroFijo viene con valor, no vuelve a pedir el número
	private Phone leerTelefono(Scanner keyboard, String numeroFijo) {
		String numero = numeroFijo != null ? numeroFijo : leerTexto(keyboard, "Número: ");
		String operador = leerTexto(keyboard, "Operador: ");
		int dniDuenio = leerEntero(keyboard, "DNI del dueño: ");
		// El dueño solo necesita su documento: los demás datos están en la tabla persona
		Person dueno = new Person(dniDuenio, "", "", Gender.OTHER);
		return new Phone(numero, operador, dueno);
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
