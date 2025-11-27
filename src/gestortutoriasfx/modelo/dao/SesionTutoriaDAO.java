package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.SesionTutoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Nombre de la Clase: SesionTutoriaDAO
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
 * Clase encargada del acceso y ejecucion de las consultas dentro de la base de datos.
 */

public class SesionTutoriaDAO {
    public static int registrarSesion(Connection conexion, SesionTutoria sesionTutoria) throws SQLException{
        if(conexion != null){
            String consulta = "INSERT INTO sesionTutoria " +
                "(idPeriodo, idTutor, matriculaEstudiante, numSesion, fecha, horaInicio, horaFin, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, sesionTutoria.getIdPeriodo());
            sentencia.setInt(2, sesionTutoria.getIdTutor());
            sentencia.setString(3, sesionTutoria.getMatriculaEstudiante());
            sentencia.setInt(4, sesionTutoria.getNumSesion());
            sentencia.setString(5, sesionTutoria.getFecha());
            sentencia.setString(6, sesionTutoria.getHoraInicio());
            sentencia.setString(7, sesionTutoria.getHoraFin());
            sentencia.setString(8, "Programada");
        
        return sentencia.executeUpdate();
        }
        throw new SQLException ("No hay conexion con la base de datos.");
    }
    
    public static ResultSet obtenerFechasPorPeriodo(Connection conexion, int idPeriodo) throws SQLException {
        if(conexion != null){
            String consulta = "SELECT * FROM fechaTutoria WHERE idPeriodo = ? ORDER BY numSesion ASC";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idPeriodo);
            return sentencia.executeQuery();
        }
        throw new SQLException ("No hay conexion con la base de datos.");
    }
    
    public static ResultSet obtenerSesionesOcupadas(Connection conexion, int idTutor, int idPeriodo) throws SQLException {
        if(conexion != null){
            String consulta = "SELECT DISTINCT numSesion FROM sesionTutoria WHERE idTutor = ? AND idPeriodo = ?";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, idPeriodo);
            return sentencia.executeQuery();
        }
        throw new SQLException ("No hay conexion con la base de datos.");
    }
}
