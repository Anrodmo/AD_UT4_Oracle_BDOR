package AD_UT4.Oracle_DBOR;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;


/**
 * 
 */
public class AccesoOracle {
	
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "System";
	private String contraseña="angel";
	private Connection conexion;
	
	public AccesoOracle(){}
	
	/**
	 *  Método que abre la conexión con la BBDD
	 */
	public void abrirConexion() {
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conexion=DriverManager.getConnection(url,
								user,contraseña);
			System.out.println("Conexion OK");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 *  Método que cierra la conexión abierta con la BBDD
	 */
	public void cerrarConexion() {
		if(this.conexion!= null) {
			try {
				this.conexion.close();
			}catch (SQLException e) {				
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 *  Método que muestra por pantalla los datos de la tabla contactos
	 */
	void mostrarContactos() {
		try {
		// Create a statement
		Statement st = conexion.createStatement();
		ResultSet resul = st.executeQuery("SELECT c.nombre, c.telefono FROM contactos c");
		System.out.println("INFORMACION DE CONTACTOS--------------");
				
		while(resul.next()){
			//aquí tambien podriamos poner resul.getInt("nif");
			System.out.printf("\nNOMBRE: %s\nTELEFONO: %s", resul.getString(1), resul.getString(2));
		}
		System.out.println("\n--------------");
		resul.close();
		st.close();
				
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	
	/**
	 * Método que inserta un estudiante nuevo en la tabla misAlumnos
	 * @param estudiante Objeto estudiante que quieres insertar en BBDD
	 * @return True si se crea con éxito, false en caso contrario.
	 */
	boolean insertaralumno(Estudiante estudiante) {
		boolean insertCorrecto = false;
		// creo una consulta que es los atributos de un objeto estudiante
		String sql = "INSERT INTO misAlumnos(id_estudiante, datos_personales) VALUES (?, ?)";
		
		try(PreparedStatement statement = conexion.prepareStatement(sql)){
			// le añado el valor del id al valor 1
			statement.setString(1,estudiante.getId_estudiante());
			
			// creo un objeto struct para los datos_erpsonales que un objeto Persona
			Object[]attributes = new Object[] {estudiante.getDatos_personales().getNombre(),
					estudiante.getDatos_personales().getTelefono()};
			// ahora hago un struct con esos datos
			Struct struct = conexion.createStruct("PERSONA", attributes);
			// ahora añado el struct a la query
			statement.setObject(2, struct);
			
			// ahora lanzo la consulta
			int res = statement.executeUpdate();
			if(res == 1)
				insertCorrecto=true;
			System.out.println("Si el codigo ha llegado hasta aqui es que se ha insertado"
					+ "correctamente, filas modificadas: "+res);			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return insertCorrecto;
	}
	
	
	/**
	 * Método que elimina un alumno de la tabla misAlumnos a partir de su nombre
	 * @param String nombre del alumno que se quiere eliminar
	 * @return True si se elimina con éxito, false en caso contrario.
	 */
	boolean eliminarAlumno(String nombre) {
		boolean operacionCorrecta = false;				
		String sqlDelete = "DELETE FROM misAlumnos WHERE estudiante.datos_personales.nombre = ?";
		
		try(PreparedStatement preparedStatement = conexion.prepareStatement(sqlDelete)){
			preparedStatement.setString(1, nombre);
			int filasAfectadas = preparedStatement.executeUpdate();
			operacionCorrecta = filasAfectadas>0;
			System.out.println("si essto sin error: "+operacionCorrecta);
		} catch (SQLException e) {
			System.out.println("Si esto error");
			e.printStackTrace();
		}
		
		
		
		return operacionCorrecta;
	}
	
	

}


