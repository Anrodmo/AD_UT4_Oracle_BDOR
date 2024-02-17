package AD_UT4.Oracle_DBOR;

import java.time.LocalDate;

public class Admitido {
	
	private LocalDate dia;
	private Estudiante matriculado;
	
	public Admitido(LocalDate dia, Estudiante matriculado) {
		super();
		this.dia = dia;
		this.matriculado = matriculado;
	}

	public LocalDate getDia() {
		return dia;
	}

	public void setDia(LocalDate dia) {
		this.dia = dia;
	}

	public Estudiante getMatriculado() {
		return matriculado;
	}

	public void setMatriculado(Estudiante matriculado) {
		this.matriculado = matriculado;
	}
	
	
	
}
