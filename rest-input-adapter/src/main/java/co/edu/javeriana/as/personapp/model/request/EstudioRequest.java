package co.edu.javeriana.as.personapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioRequest {
	private String idProfesion;
	private String dniPersona;
	// Formato yyyy-MM-dd; vacío o null si no se conoce
	private String fechaGraduacion;
	private String universidad;
}
