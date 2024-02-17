package AD_UT4.Oracle_DBOR;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.time.LocalDate;
import java.util.ArrayList;


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
			//aquí también podríamos poner resul.getInt("nif");
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
					+ " correctamente, filas modificadas: "+res);			
			
		} catch (SQLException e) {
			
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
		String sqlDelete = "DELETE FROM misAlumnos a WHERE a.datos_personales.nombre = ?";
		
		try(PreparedStatement preparedStatement = conexion.prepareStatement(sqlDelete)){
			preparedStatement.setString(1, nombre);
			int filasAfectadas = preparedStatement.executeUpdate();
			
			operacionCorrecta = filasAfectadas>0;
			System.out.println("si esto sin error, resultado: "+operacionCorrecta);
		} catch (SQLException e) {
			System.out.println("Si esto error");			
			e.printStackTrace();
		}						
		return operacionCorrecta;
	}
	
	/**
	 * Método que muestra el teléfono de todos los alumnos que tenga el nombre facilitado
	 * @param nombre -> nombre del alumno cuyo teléfono e quiere conocer
	 * @return  True -> si no hay excepción y la consulta da al  menos un resultado.
	 */
	public boolean obtenerTelefonoAlumno(String nombre) {
	    boolean operacionCorrecta = false;
	    String telefono = null;
	    String sqlSelect = "SELECT a.datos_personales.telefono FROM misAlumnos a WHERE a.datos_personales.nombre = ?";

	    try (PreparedStatement preparedStatement = conexion.prepareStatement(sqlSelect)) {
	        preparedStatement.setString(1, nombre);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {	            	
	                telefono = resultSet.getString("datos_personales.telefono"); // aquí recojo de la columna por nnombre
	                // teléfono = resultSet.getString(1);  // aquí recojo de la primera columna
	                System.out.println("Teléfono de " + nombre + ": " + telefono);               	            		            	
	                operacionCorrecta = true;
	            } else {
	                System.out.println("No se encontró ningún alumno con el nombre: " + nombre);
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error al obtener el teléfono del alumno.");
	        e.printStackTrace();
	    }
	    return operacionCorrecta;
	}
		
		
	
	/**
	 * Método similar al anterior pero aprovecha la ORDB para obtener objetos estudiante y los devuelvo
	 * en un ArrayList
	 * @param nombre  - nombre del que se quiere obtener los objetos Persona
	 * @return ArrayList<Estudiante> con los resultados
	 */
	public ArrayList<Estudiante> obtenerEstudiantesPorNombre(String nombre) {
	    String sqlSelect = "SELECT a.id_estudiante, a.datos_personales FROM misAlumnos a WHERE a.datos_personales.nombre = ?";
	    ArrayList<Estudiante> listadoEstudiantes = new ArrayList<>();

	    try (PreparedStatement preparedStatement = conexion.prepareStatement(sqlSelect)) {
	        preparedStatement.setString(1, nombre);
	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            while (resultSet.next()) {
	                // Recupero el id
	                String id = resultSet.getString("id_estudiante");
	                // Recupero los tipos PERSONA de la consulta
	                Struct struct = (Struct) resultSet.getObject("datos_personales");

	                // obtengo los atributos de la estructura
	                Object[] attributes = struct.getAttributes();
	                String nombrePersona = (String) attributes[0];
	                String telefonoPersona = (String) attributes[1];
	                // Creo un objeto Persona
	                Persona persona = new Persona(nombrePersona, telefonoPersona);
	                // Creo un objeto Estudiante con id y Persona
	                Estudiante estudiante = new Estudiante(id, persona);
	                listadoEstudiantes.add(estudiante);

	                System.out.println("Estudiante encontrado: " + estudiante.getId_estudiante()+", "
		            		+estudiante.getDatos_personales().getNombre()+", "+estudiante.getDatos_personales().getTelefono());
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error al obtener los estudiantes desde la base de datos.");
	        e.printStackTrace();
	    }
	    return listadoEstudiantes;
	}
	
	
	
	/**
	 * Método que muestra y devuelve un ArrayList<Estudiante>  con los objetos de la tabla misalumnos
	 * @return ArrayList<Estudiante>  con todos los registros de la tabla misalumnos
	 */
	public ArrayList<Estudiante> obtenerEstudiantes() {
	    String sqlSelect = "SELECT a.id_estudiante, a.datos_personales FROM misAlumnos a";
	    ArrayList<Estudiante> listadoEstudiantes = new ArrayList<>();

	    try (PreparedStatement preparedStatement = conexion.prepareStatement(sqlSelect);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        while (resultSet.next()) {
	            // Recupero el id
	            String id = resultSet.getString("id_estudiante");
	            // Recupero los tipos PERSONA de la consulta
	            Struct struct = (Struct) resultSet.getObject("datos_personales");
	            // Obtengo los atributos de la estructura
	            Object[] attributes = struct.getAttributes();
	            String nombrePersona = (String) attributes[0];
	            String telefonoPersona = (String) attributes[1];
	            // Creo un objeto Persona
	            Persona persona = new Persona(nombrePersona, telefonoPersona);
	            // Crear un objeto Estudiante con id y Persona
	            Estudiante estudiante = new Estudiante(id, persona);
	            listadoEstudiantes.add(estudiante);

	            System.out.println("Estudiante encontrado: " + estudiante.getId_estudiante()+", "
	            		+estudiante.getDatos_personales().getNombre()+", "+estudiante.getDatos_personales().getTelefono());
	        }

	    } catch (SQLException e) {
	        System.out.println("Error al obtener los estudiantes desde la base de datos.");
	        e.printStackTrace();
	    }
	    return listadoEstudiantes;
	}
	
	/**
	 * Método que muestra y devuelve un ArrayList<Admitido>  con los objetos de la tabla admitidos
	 * @return ArrayList<Admitido>  con todos los registros de la tabla admitidos
	 */
	public ArrayList<Admitido> obtenerAdmitidos() {
	    String sqlSelect = "SELECT a.dia, a.matriculado FROM admitidos a";
	    ArrayList<Admitido> listadoAdmitidos = new ArrayList<>();

	    try (PreparedStatement preparedStatement = conexion.prepareStatement(sqlSelect);
	         ResultSet resultSet = preparedStatement.executeQuery()) {

	        while (resultSet.next()) {
	            // Recupero el id
	            LocalDate dia = resultSet.getDate("dia").toLocalDate();
	            // Recupero los tipos ESTUDIANTE de la consulta
	            Struct struct = (Struct) resultSet.getObject("matriculado");
	            // Obtengo los atributos de la estructura
	            Object[] attributes = struct.getAttributes();
	            String id_estudiante = (String) attributes[0];
	            
	            Struct struct2 = (Struct) attributes[1];
	            Object[] attributes2 = struct2.getAttributes();
	            String nombrePersona = (String) attributes2[0];
	            String telefonoPersona = (String) attributes2[1];
	            // Creo un objeto Persona
	            Persona persona = new Persona(nombrePersona, telefonoPersona);
	            // Creo un objeto Estudiante con id y Persona
	            Estudiante estudiante = new Estudiante(id_estudiante, persona);
	            // Creo un objeto Admitido
	            Admitido admitido = new Admitido(dia, estudiante);
	            listadoAdmitidos.add(admitido);

	            System.out.println("Admitido encontrado: "+admitido.getDia()+", " 
	            		+ admitido.getMatriculado().getId_estudiante()+", "
	            		+admitido.getMatriculado().getDatos_personales().getNombre()+", "
	            		+admitido.getMatriculado().getDatos_personales().getTelefono());
	        }

	    } catch (SQLException e) {
	        System.out.println("Error al obtener los estudiantes desde la base de datos.");
	        e.printStackTrace();
	    }
	    return listadoAdmitidos;
	}
	
	

}


