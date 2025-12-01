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
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 26/11/2025
 *
 * Descripción:
 * Se encarga de administrar la informacion de los distintos metodos de recuperacion de informacion del 
 * estudiante de la base de datos.
 */

public class EstudianteImplementacion {
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
}