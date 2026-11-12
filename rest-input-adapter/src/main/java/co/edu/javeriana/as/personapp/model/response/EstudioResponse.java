package co.edu.javeriana.as.personapp.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioResponse {
	private String idProfesion;
	private String dniPersona;
	private String fechaGraduacion;
	private String universidad;
	private String database;
	private String status;
}
