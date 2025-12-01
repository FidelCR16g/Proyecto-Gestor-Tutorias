package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.UsuarioDAO;
import gestortutoriasfx.modelo.pojo.Usuario;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Nombre de la Clase: UsuarioImplementacion
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
 * Se encarga de administrar la informacion de los distintos metodos de recuperacion de informacion de los 
 * usuarios de la base de datos.
 */

public class UsuarioImplementacion {
    public static HashMap<String, Object> obtenerUsuarios(String nombreUsuario) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexion = null;
        
        try {
            conexion = ConexionBD.abrirConexionBD();
            
            Usuario usuario = UsuarioDAO.obtenerUsuario(conexion, nombreUsuario);
            
            if (usuario != null) {
                respuesta.put("error", false);
                respuesta.put("usuario", usuario);
            } else {
                respuesta.put("mensaje", "Usuario no encontrado o credenciales incorrectas.");
            }
            
        } catch (SQLException e) {
            respuesta.put("mensaje", "Error de conexión a BD: " + e.getMessage());
            e.printStackTrace();
        } finally {
            
            ConexionBD.cerrarConexion(conexion);
        }
        
        return respuesta;
    }
}