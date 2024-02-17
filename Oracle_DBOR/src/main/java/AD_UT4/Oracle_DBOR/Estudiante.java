package AD_UT4.Oracle_DBOR;

public class Estudiante {
	private String id_estudiante;
	private Persona datos_personales;
	
	public Estudiante(String id_estudiante, Persona datos_personales) {
		super();
		this.id_estudiante = id_estudiante;
		this.datos_personales = datos_personales;
	}
	public String getId_estudiante() {
		return id_estudiante;
	}
	public void setId_Estudiante(String id_estudiante) {
		this.id_estudiante = id_estudiante;
	}
	public Persona getDatos_personales() {
		return datos_personales;
	}
	public void setDatos_personales(Persona datos_personales) {
		this.datos_personales = datos_personales;
	}
	
	
	
}
