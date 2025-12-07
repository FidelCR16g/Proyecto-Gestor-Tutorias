package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.Estudiante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    
    public static ArrayList<Estudiante> obtenerTutoradosPorTutor(Connection conexion, int idTutor) throws SQLException{
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        ArrayList<Estudiante> listaEstudiantes = new ArrayList<>();
        
        String consulta = "SELECT e.*, pe.nombre AS nombrePrograma " + 
                          "FROM estudiante e " +
                          "INNER JOIN asignacionTutorado a ON e.matricula = a.matriculaEstudiante " +
                          "INNER JOIN programaEducativo pe ON e.idProgramaEducativo = pe.idProgramaEducativo " +
                          "WHERE a.idTutor = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    Estudiante estudiante = new Estudiante();
                    estudiante.setMatricula(resultado.getString("matricula"));
                    estudiante.setNombre(resultado.getString("nombre"));
                    estudiante.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                    estudiante.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                    estudiante.setCorreoInstitucional(resultado.getString("correoInstitucional"));
                    estudiante.setSemestre(resultado.getInt("semestre"));
                    estudiante.setIdProgramaEducativo(resultado.getInt("idProgramaEducativo"));
                    estudiante.setNombreProgramaEducativo(resultado.getString("nombrePrograma"));
                    estudiante.setSituacionRiesgo(resultado.getBoolean("situacionRiesgo"));
                    listaEstudiantes.add(estudiante);
                }
            }
        }
        return listaEstudiantes;
    }
    
    public static ArrayList<Estudiante> obtenerEstudiantesSinTutor(Connection conexion) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        ArrayList<Estudiante> estudiantes = new ArrayList<>();
        
        String consulta = "SELECT e.*, pe.nombre AS nombrePrograma " +
                "FROM estudiante e " +
                "INNER JOIN programaEducativo pe ON e.idProgramaEducativo = pe.idProgramaEducativo " +
                "LEFT JOIN asignacionTutorado a ON e.matricula = a.matriculaEstudiante " +
                "WHERE a.idAsignacionTutorado IS NULL " +
                "ORDER BY e.semestre, e.apellidoPaterno";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta);
                ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setMatricula(resultado.getString("matricula"));
                estudiante.setNombre(resultado.getString("nombre"));
                estudiante.setApellidoPaterno(resultado.getString("apellidoPaterno"));
                estudiante.setApellidoMaterno(resultado.getString("apellidoMaterno"));
                estudiante.setSemestre(resultado.getInt("semestre"));
                estudiante.setNombreProgramaEducativo(resultado.getString("nombrePrograma"));
                estudiantes.add(estudiante);
                }
            }
        return estudiantes;
    }
    
    public static int asignarTutor(Connection conexion, String matricula, int idTutor) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        String consulta = "INSERT INTO asignacionTutorado (idTutor, matriculaEstudiante) VALUES (?, ?)";
        int filasAfectadas = 0;
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setString(2, matricula);
            filasAfectadas = sentencia.executeUpdate();
        }
        return filasAfectadas;
    }

    public static int desasignarTutor(Connection conexion, String matricula, int idTutor) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        int filasAfectadas = 0;
        
        String consulta = "DELETE FROM asignacionTutorado WHERE idTutor = ? AND matriculaEstudiante = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setString(2, matricula);
            filasAfectadas = sentencia.executeUpdate();
        }
        return filasAfectadas;
    }
}
