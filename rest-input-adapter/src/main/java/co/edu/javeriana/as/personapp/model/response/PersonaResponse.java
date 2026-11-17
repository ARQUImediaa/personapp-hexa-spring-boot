package co.edu.javeriana.as.personapp.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import co.edu.javeriana.as.personapp.model.request.PersonaRequest;

public class PersonaResponse extends PersonaRequest{
	
	private String status;

	// Solo se llenan en las consultas (GET); en POST y PUT quedan null y no salen en el JSON
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<TelefonoDetalle> telefonos;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<EstudioDetalle> estudios;
	
	public PersonaResponse(String dni, String firstName, String lastName, String age, String sex, String database, String status) {
		super(dni, firstName, lastName, age, sex, database);
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<TelefonoDetalle> getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(List<TelefonoDetalle> telefonos) {
		this.telefonos = telefonos;
	}

	public List<EstudioDetalle> getEstudios() {
		return estudios;
	}

	public void setEstudios(List<EstudioDetalle> estudios) {
		this.estudios = estudios;
	}

	public static class TelefonoDetalle {
		private String numero;
		private String operador;

		public TelefonoDetalle(String numero, String operador) {
			this.numero = numero;
			this.operador = operador;
		}

		public String getNumero() {
			return numero;
		}

		public String getOperador() {
			return operador;
		}
	}

	public static class EstudioDetalle {
		private String idProfesion;
		private String profesion;
		private String descripcionProfesion;
		private String fechaGraduacion;
		private String universidad;

		public EstudioDetalle(String idProfesion, String profesion, String descripcionProfesion,
				String fechaGraduacion, String universidad) {
			this.idProfesion = idProfesion;
			this.profesion = profesion;
			this.descripcionProfesion = descripcionProfesion;
			this.fechaGraduacion = fechaGraduacion;
			this.universidad = universidad;
		}

		public String getIdProfesion() {
			return idProfesion;
		}

		public String getProfesion() {
			return profesion;
		}

		public String getDescripcionProfesion() {
			return descripcionProfesion;
		}

		public String getFechaGraduacion() {
			return fechaGraduacion;
		}

		public String getUniversidad() {
			return universidad;
		}
	}
}
