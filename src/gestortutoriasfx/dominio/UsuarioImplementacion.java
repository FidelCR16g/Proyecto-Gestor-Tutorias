package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.UsuarioDAO;
import gestortutoriasfx.modelo.pojo.Usuario;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public static HashMap<String, Object> validarLogin(String noPersonal, String password) {
    HashMap<String, Object> respuesta = new LinkedHashMap<>();
    respuesta.put("error", true);

    Connection conexion = ConexionBD.abrirConexionBD();

    if (conexion != null) {
        try {
            Usuario usuario = UsuarioDAO.iniciarSesion(conexion, noPersonal, password);

            if (usuario != null) {
                respuesta.put("error", false);
                respuesta.put("usuario", usuario);
            } else {
                respuesta.put("mensaje", "Credenciales incorrectas.");
            }

        } catch (SQLException ex) {
            respuesta.put("mensaje", "Error BD: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(conexion);
        }
    } else {
        respuesta.put("mensaje", "No hay conexión con la base de datos.");
    }

    return respuesta;
}

    
    public static HashMap<String, Object> obtenerRolesUsuario(int idUsuario) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexion = ConexionBD.abrirConexionBD();
        
        if (conexion != null) {
            try {
                ArrayList<String> roles = UsuarioDAO.obtenerRoles(conexion, idUsuario);
                respuesta.put("error", false);
                respuesta.put("roles", roles);
            } catch (SQLException e) {
                respuesta.put("mensaje", "Error SQL al obtener roles: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "No se pudo establecer conexión con la base de datos.");
        }
        
        return respuesta;
    }
}