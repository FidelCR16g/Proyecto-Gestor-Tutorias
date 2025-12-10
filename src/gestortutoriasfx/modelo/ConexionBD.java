package gestortutoriasfx.modelo;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Nombre de la Clase: ConexionBD
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 25/11/2025
 *
 * Descripción:
 * Clase encargada de establecer y cerrar conexion con la base de datos.
 */

public class ConexionBD {
    private static final String ARCHIVO_PROPIEDADES = "/gestortutoriasfx/recurso/database.properties";
    private static String url;
    private static String driver;
    private static String usuarioActual;
    private static String passwordActual;
    
    static {
        establecerCredenciales("login_checker");
    }
    
    public static Connection abrirConexionBD() {
        Connection conexion = null;
        try{
            Class.forName(driver);
            conexion = DriverManager.getConnection(url, usuarioActual, passwordActual);
        } catch (ClassNotFoundException ex){
            System.err.println("Error: No se encontró el Driver de MySQL.");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.err.println("Error al conectar con MySQL. Verifique URL, usuario o password.");
            ex.printStackTrace();
        }
        return conexion;
    }
    
    public static void establecerCredenciales(String rolKey) {
        try {
            Properties prop = cargarPropiedades();
            
            if (url == null) {
                driver = prop.getProperty("db.driver");
                url = prop.getProperty("db.url");
            }
            
            String userKey = "db.user." + rolKey.toLowerCase();
            String passKey = "db.password." + rolKey.toLowerCase();

            String nuevoUsuario = prop.getProperty(userKey);
            String nuevoPassword = prop.getProperty(passKey);

            if (nuevoUsuario != null && nuevoPassword != null) {
                usuarioActual = nuevoUsuario;
                passwordActual = nuevoPassword;
            } else {
                System.err.println("ADVERTENCIA: No se encontraron credenciales para el rol: " + rolKey);
            }

        } catch (IOException ex) {
            System.err.println("Error crítico al cargar configuración de seguridad: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private static Properties cargarPropiedades() throws IOException{
        Properties prop = new Properties();
        try(InputStream input = ConexionBD.class.getResourceAsStream(ARCHIVO_PROPIEDADES)){
            if(input == null){
                throw new IOException("No se encontro el archivo " + ARCHIVO_PROPIEDADES);
            }
            prop.load(input);
        }
        return prop;
    }
    
    public static void cerrarConexion(Connection conexion){
        if(conexion != null){
            try{
                if(!conexion.isClosed()){
                    conexion.close();
                }
            }catch(SQLException ex){
                System.err.print("Error al cerrar la conexion " + ex.getMessage());
            }finally{
                conexion = null;
            }
        }
    }
}
