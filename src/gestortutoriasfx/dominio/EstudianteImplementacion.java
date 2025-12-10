package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.EstudianteDAO;
import gestortutoriasfx.modelo.pojo.Estudiante;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Nombre de la Clase: EstudianteImplementacion
 *
 * Proyecto: Sistema de Gestudianteión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 26/11/2025
 *
 * Descripción:
 * Se encarga de administrar la informacion de los distintos metodos de recuperacion de informacion del 
 * estudianteudiante de la base de datos.
 */

public class EstudianteImplementacion {
    public static boolean actualizarAsignaciones(ArrayList<Estudiante> nuevos, ArrayList<Estudiante> removidos, int idTutor) {
        boolean exito = false;
        Connection conexion = ConexionBD.abrirConexionBD();
        
        if (conexion != null) {
            try {
                conexion.setAutoCommit(false);
                if (nuevos != null) {
                    for (Estudiante estudiante : nuevos) {
                        EstudianteDAO.asignarTutor(conexion, estudiante.getMatricula(), idTutor);
                    }
                }
                if (removidos != null) {
                    for (Estudiante estudiante : removidos) {
                        EstudianteDAO.desasignarTutor(conexion, estudiante.getMatricula(), idTutor);
                    }
                }
                
                conexion.commit();
                exito = true;
                
            } catch (SQLException e) {
                e.printStackTrace();
                try { conexion.rollback(); } catch (SQLException ex) { }
            } finally {
                try { conexion.setAutoCommit(true); } catch (SQLException ex) { }
                ConexionBD.cerrarConexion(conexion);
            }
        }
        return exito;
    }
    
    public static HashMap<String, Object> obtenerTutoradosPorTutor(int idTutor) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;
        
        try {
            conexion = ConexionBD.abrirConexionBD();
            ArrayList<Estudiante> estudiantes = EstudianteDAO.obtenerTutoradosPorTutor(conexion, idTutor);
            respuesta.put("error", false);
            respuesta.put("estudiantes", estudiantes);
        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(conexion);
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerEstudiantesSinTutor() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        
        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion != null) {
            try {
                ArrayList<Estudiante> estudiantes = EstudianteDAO.obtenerEstudiantesSinTutor(conexion);
                respuesta.put("error", false);
                respuesta.put("estudiantes", estudiantes);
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