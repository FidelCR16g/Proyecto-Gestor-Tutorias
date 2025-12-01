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
        
        String consulta = "SELECT e.*, c.nombre AS nombreCarrera " + 
                          "FROM estudiante e " +
                          "INNER JOIN asignacionTutorado a ON e.matricula = a.matriculaEstudiante " +
                          "INNER JOIN carrera c ON e.idCarrera = c.idCarrera " +
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
                    estudiante.setCarrera(resultado.getString("nombreCarrera"));
                    estudiante.setSemestre(resultado.getInt("semestre"));
                    estudiante.setIdCarrera(resultado.getInt("idCarrera"));
                    listaEstudiantes.add(estudiante);
                }
            }
        }
        return listaEstudiantes;
    }
}
