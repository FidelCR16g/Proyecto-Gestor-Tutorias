package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.ProblematicaAcademica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Nombre de la Clase: ProblematicaAcademicaDAO
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 05/12/2025
 *
 * Descripción:
 * Clase encargada del acceso y ejecucion de las consultas dentro de la base de datos.
 */

public class ProblematicaAcademicaDAO {

    public static int registrarProblematica(Connection conexion, ProblematicaAcademica problematicaAcademica, int idReporte) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        int filas = 0;

        String consulta = "INSERT INTO problemasAcademicosReportadosTutoria " +
                  "(idReporteTutoria, nombreExperienciaEducativa, nombreProfesor, problema, numEstudiantes) " +
                  "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idReporte);
            sentencia.setString(2, problematicaAcademica.getNombreExperienciaEducativa());
            sentencia.setString(3, problematicaAcademica.getNombreProfesor());
            sentencia.setString(4, problematicaAcademica.getProblema());
            sentencia.setInt(5, problematicaAcademica.getNumEstudiantes());
            filas = sentencia.executeUpdate();
        }
        return filas;
    }

    public static int eliminarProblematicasPorReporte(Connection conexion, int idReporte) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        int filas = 0;

        String consulta = "DELETE FROM problemasAcademicosReportadosTutoria WHERE idReporteTutoria = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idReporte);
            filas = sentencia.executeUpdate();
        }
        return filas;
    }

    public static ArrayList<ProblematicaAcademica> obtenerProblematicasPorReporte(Connection conexion, int idReporte) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<ProblematicaAcademica> lista = new ArrayList<>();

        String consulta = "SELECT idProblemasAcademicosReportadosTutoria, idReporteTutoria, idExperienciaEducativa, "
                + "nombreExperienciaEducativa, nombreProfesor, problema, numEstudiantes "
                + "FROM problemasAcademicosReportadosTutoria "
                + "WHERE idReporteTutoria = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idReporte);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    ProblematicaAcademica p = new ProblematicaAcademica();
                    
                    p.setIdProblematicaAcademica(resultado.getInt("idProblemasAcademicosReportadosTutoria"));
                    p.setIdReporteTutoria(resultado.getInt("idReporteTutoria"));
                    
                    int idEE = resultado.getInt("idExperienciaEducativa");
                    p.setIdExperienciaEducativa(resultado.wasNull() ? 0 : idEE);
                    
                    p.setNombreExperienciaEducativa(resultado.getString("nombreExperienciaEducativa"));
                    p.setNombreProfesor(resultado.getString("nombreProfesor"));
                    p.setProblema(resultado.getString("problema"));
                    p.setNumEstudiantes(resultado.getInt("numEstudiantes"));
                    
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}