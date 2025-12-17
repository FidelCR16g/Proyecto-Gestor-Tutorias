package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.TutorDAO;
import gestortutoriasfx.modelo.pojo.Tutor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
    
    public static HashMap<String, Object> obtenerIdTutor(int idUsuario) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexion = ConexionBD.abrirConexionBD();
        
        if (conexion != null) {
            try {
                int idTutor = TutorDAO.obtenerIdTutor(conexion, idUsuario);
                
                if (idTutor > 0) {
                    respuesta.put("error", false);
                    respuesta.put("idTutor", idTutor);
                } else {
                    respuesta.put("mensaje", "No se encontró información del tutor asociado a este usuario.");
                }
                
            } catch (SQLException e) {
                respuesta.put("mensaje", "Error BD: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "No hay conexión con la base de datos.");
        }
        
        return respuesta;
    }

    public static HashMap<String, Object> obtenerListaTutores() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexion = ConexionBD.abrirConexionBD();
        
        if (conexion != null) {
            try {
                ArrayList<Tutor> lista = TutorDAO.obtenerTutoresDisponibles(conexion);
                respuesta.put("error", false);
                respuesta.put("tutores", lista);
            } catch (SQLException e) {
                respuesta.put("mensaje", "Error BD: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "Sin conexión.");
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerTutoresPorProgramaEducativo(int idProgramaEducativo) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);

        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion != null) {
            try {
                ArrayList<Tutor> lista = TutorDAO.obtenerTutoresPorProgramaEducativo(conexion, idProgramaEducativo);
                respuesta.put("error", false);
                respuesta.put("tutores", lista);
            } catch (SQLException e) {
                respuesta.put("mensaje", "Error BD: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "Sin conexión.");
        }

        return respuesta;
    }

}