package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.TutorDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Nombre de la Clase: TutorImplementacion
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
 * Se encarga de administrar la informacion de los distintos metodos de recuperacion de informacion del 
 * tutor de la base de datos.
 */

public class TutorImplementacion {
    
    public static HashMap<String, Object> obtenerIdTutor(int idUsuario){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;
        
        try{
            conexion = ConexionBD.abrirConexionBD();
            ResultSet resultado = TutorDAO.obtenerIdTutor(conexion, idUsuario);
            
            if(resultado.next()) {
                respuesta.put("error", false);
                respuesta.put("idTutor", resultado.getInt("idTutor"));
            }else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se encontró información del tutor asociado.");
            }
        }catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error BD: " + e.getMessage());
        }finally {
            ConexionBD.cerrarConexion(conexion);
        }
        return respuesta;
    }
}