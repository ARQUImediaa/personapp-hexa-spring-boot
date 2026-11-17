package co.edu.javeriana.as.personapp.terminal.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonaModelCli {
	private Integer cc;
	private String nombre;
	private String apellido;
	private String genero;
	private Integer edad;
	// Solo se llenan en las consultas (listar y buscar); en crear y editar quedan null
	private List<String> telefonos;
	private List<String> estudios;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("PersonaModelCli(cc=" + cc + ", nombre=" + nombre + ", apellido=" + apellido
				+ ", genero=" + genero + ", edad=" + edad + ")");
		if (telefonos != null)
			sb.append("\n    Teléfonos: ").append(telefonos.isEmpty() ? "(ninguno)" : String.join(", ", telefonos));
		if (estudios != null) {
			sb.append("\n    Estudios: ").append(estudios.isEmpty() ? "(ninguno)" : "");
			estudios.forEach(e -> sb.append("\n      - ").append(e));
		}
		return sb.toString();
	}
}
