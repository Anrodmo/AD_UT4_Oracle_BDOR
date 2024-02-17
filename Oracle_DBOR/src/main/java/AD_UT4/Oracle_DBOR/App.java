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
		System.out.println("\n==========================================\n");
		a.obtenerAdmitidos();
		a.cerrarConexion();
		
		
		/*
		a.insertaralumno(new Estudiante("ID004",new Persona("Rosa","666124578")));	
		a.obtenerTelefonoAlumno("Felipe");   // consulta tradicional
		a.obtenerTelefonoAlumno("Felipe");
		a.eliminarAlumno("Rosa");	
		System.out.println("");
		a.obtenerEstudiantes();	
		a.obtenerEstudiantesPorNombre("Andrés");
		System.out.println("");
		*/
		
			
    }
}
