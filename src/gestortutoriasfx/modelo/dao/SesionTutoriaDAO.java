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
    public static int actualizarEstadoAsistencia(Connection conexion, int idSesion, boolean asistio) throws SQLException {
        if(conexion != null){
            String estado = asistio ? "Asistio" : "No Asistio";
            String consulta = "UPDATE sesionTutoria SET estado = ? WHERE idSesion = ?";
        
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setString(1, estado);
            sentencia.setInt(2, idSesion);
        
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
    
    public static ResultSet obtenerSesionesAgrupadasPorTutor(Connection conexion, int idTutor) throws SQLException {
        if(conexion != null){
            String consulta = "SELECT s.numSesion, p.nombre AS nombrePeriodo, MIN(s.fecha) AS fecha " +
                     "FROM sesionTutoria s " +
                     "INNER JOIN periodoEscolar p ON s.idPeriodo = p.idPeriodo " +
                     "WHERE s.idTutor = ? " +
                     "GROUP BY s.numSesion, p.nombre " +
                     "ORDER BY s.numSesion ASC";
                     
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idTutor);
            return sentencia.executeQuery();
        }
        throw new SQLException ("No hay conexion con la base de datos.");
    }
    
    public static ResultSet obtenerAlumnosPorSesion(Connection conexion, int idTutor, int numSesion) throws SQLException {
        if(conexion != null){
            String consulta = "SELECT st.idSesion, st.matriculaEstudiante, st.estado, " +
                         "e.nombre, e.apellidoPaterno, e.apellidoMaterno, c.nombre AS carrera " +
                         "FROM sesionTutoria st " +
                         "INNER JOIN estudiante e ON st.matriculaEstudiante = e.matricula " +
                        "INNER JOIN carrera c ON e.idCarrera = c.idCarrera " +
                        "WHERE st.idTutor = ? AND st.numSesion = ?";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, numSesion);    
            return sentencia.executeQuery();   
        }
        throw new SQLException ("No hay conexion con la base de datos.");
    }
    
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
}
