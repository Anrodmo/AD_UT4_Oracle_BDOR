package AD_UT4.Oracle_DBOR;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	AccesoOracle a = new AccesoOracle();
		a.abrirConexion();   
		
		// a.insertaralumno(new Estudiante("ID002",new Persona("Felipe","967124578")));
		// a.eliminarAlumno("juan");
		
		a.obtenerTelefonoAlumno("Felipe");   // consulta tradicional
		
		System.out.println("");
		a.obtenerEstudiantesPorNombre("Andrés");
		System.out.println("");
		a.obtenerEstudiantes();
		
		a.cerrarConexion();
		
			
    }
}
