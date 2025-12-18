package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.Tutor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Nombre de la Clase: TutorDAO
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Univeresultadoidad Veracruzana
 * Curesultadoo: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 25/11/2025
 *
 * Descripción:
 * Clase encargada del acceso y ejecucion de las consultas dentro de la base de datos.
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TutorDAO {

    public static int obtenerIdTutor(Connection conexion, int idUsuario) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        int idTutor = 0;
        
        String consulta = "SELECT t.idTutor " +
                          "FROM tutor t " +
                          "INNER JOIN profesor p ON t.idProfesor = p.idProfesor " +
                          "INNER JOIN usuario u ON p.numPersonal = u.numPersonal " +
                          "WHERE u.idUsuario = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idUsuario);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    idTutor = resultado.getInt("idTutor");
                }
            }
        }
        return idTutor;
    }

    public static ArrayList<Tutor> obtenerTutoresDisponibles(Connection conexion) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<Tutor> tutores = new ArrayList<>();
        
        String consulta =
                "SELECT t.idTutor, t.idProfesor, t.espaciosTutorados, " +
                "p.numPersonal, p.nombre, p.apellidoPaterno, p.apellidoMaterno, p.email " + 
                "FROM tutor t " +
                "INNER JOIN profesor p ON t.idProfesor = p.idProfesor " +
                "ORDER BY p.nombre, p.apellidoPaterno";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                tutores.add(mapearTutor(resultado));
            }
        }
        return tutores;
    }
    
    public static ArrayList<Tutor> obtenerTutoresPorProgramaEducativo(Connection conexion, int idProgramaEducativo) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<Tutor> tutores = new ArrayList<>();
        
        String consulta =
            "SELECT DISTINCT t.idTutor, t.idProfesor, t.espaciosTutorados, " +
            "p.numPersonal, p.nombre, p.apellidoPaterno, p.apellidoMaterno, p.email " +
            "FROM programaEducativo pe " +
            "INNER JOIN facultad f ON f.idFacultad = pe.idFacultad " +
            "INNER JOIN ubicacionProfesor up ON up.idFacultad = f.idFacultad " +
            "INNER JOIN profesor p ON p.idProfesor = up.idProfesor " +
            "INNER JOIN tutor t ON t.idProfesor = p.idProfesor " +
            "WHERE pe.idProgramaEducativo = ? " +
            "ORDER BY p.nombre, p.apellidoPaterno, p.apellidoMaterno";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idProgramaEducativo);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    tutores.add(mapearTutor(resultado));
                }
            }
        }

        return tutores;
    }

    private static Tutor mapearTutor(ResultSet rs) throws SQLException {
        Tutor tutor = new Tutor();
        tutor.setIdTutor(rs.getInt("idTutor"));
        tutor.setIdProfesor(rs.getInt("idProfesor"));
        tutor.setEspaciosTutorados(rs.getInt("espaciosTutorados"));
        
        tutor.setNumPersonal(rs.getString("numPersonal"));
        tutor.setNombre(rs.getString("nombre"));
        tutor.setApellidoPaterno(rs.getString("apellidoPaterno"));
        tutor.setApellidoMaterno(rs.getString("apellidoMaterno"));
        
        tutor.setCorreoInstitucional(rs.getString("email")); 

        String nombreCompleto = rs.getString("nombre") + " " +
                                rs.getString("apellidoPaterno") + " " +
                                rs.getString("apellidoMaterno");
        tutor.setNombre(nombreCompleto);

        return tutor;
    }
}