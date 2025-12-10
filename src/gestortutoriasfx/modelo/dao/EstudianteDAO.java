package gestortutoriasfx.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Nombre de la Clase: EstudianteDAO
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
 * Clase encargada del acceso y ejecucion de las consultas dentro de la base de datos.
 */
public class EstudianteDAO {
    
    public static ResultSet obtenerTutoradosPorTutor(Connection conexion, int idTutor) throws SQLException{
        if(conexion != null){
            String consulta = "SELECT e.*, c.nombre AS nombreCarrera " + 
                "FROM estudiante e " +
                "INNER JOIN asignacionTutorado a ON e.matricula = a.matriculaEstudiante " +
                "INNER JOIN carrera c ON e.idCarrera = c.idCarrera " +
                "WHERE a.idTutor = ?";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idTutor);
            return sentencia.executeQuery();
        }
        throw new SQLException ("No hay conexion con la base de datos.");
    }
}
