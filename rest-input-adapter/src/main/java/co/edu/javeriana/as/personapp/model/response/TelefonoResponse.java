package co.edu.javeriana.as.personapp.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelefonoResponse {
	private String numero;
	private String operador;
	private String dniDuenio;
	private String database;
	private String status;
}
