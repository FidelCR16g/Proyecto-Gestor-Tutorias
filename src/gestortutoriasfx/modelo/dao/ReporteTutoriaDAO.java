package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.ReporteTutoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class ReporteTutoriaDAO {
    public static ReporteTutoria obtenerReporte(Connection conexion, int idTutor, int idPeriodo, int numSesion) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        ReporteTutoria reporte = null;
        String consulta = "SELECT * FROM reporteTutoria WHERE idTutor = ? AND idPeriodoEscolar = ? AND numSesion = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, idPeriodo);
            sentencia.setInt(3, numSesion);
            
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    reporte = new ReporteTutoria();
                    reporte.setIdReporteTutoria(resultado.getInt("idReporteTutoria"));
                    reporte.setIdTutor(resultado.getInt("idTutor"));
                    reporte.setIdPeriodoEscolar(resultado.getInt("idPeriodoEscolar"));
                    reporte.setIdProgramaEducativo(resultado.getInt("idProgramaEducativo"));
                    reporte.setNumSesion(resultado.getInt("numSesion"));
                    reporte.setEstatus(resultado.getString("estatus"));
                    reporte.setComentarios(resultado.getString("comentarios"));
                    reporte.setNumAlumnosAsistieron(resultado.getInt("numAlumnosAsistieron"));
                    reporte.setNumAlumnosRiesgo(resultado.getInt("numAlumnosRiesgo"));
                    reporte.setNombreTutor(resultado.getString("nombreTutor"));
                    reporte.setNombrePrograma(resultado.getString("nombrePrograma"));
                    reporte.setNombrePeriodoEscolar(resultado.getString("nombrePeriodoEscolar"));
                }
            }
        }
        return reporte;
    }

    public static int registrarReporte(Connection conexion, ReporteTutoria reporte) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        int idGenerado = 0; 
        
        String consulta = "INSERT INTO reporteTutoria "
                       + "(idTutor, idPeriodoEscolar, idProgramaEducativo, numSesion, estatus, comentarios, "
                       + "numAlumnosAsistieron, numAlumnosRiesgo, fechaPrimeraTutoria, fechaUltimaTutoria, estadoLugar) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setInt(1, reporte.getIdTutor());
            sentencia.setInt(2, reporte.getIdPeriodoEscolar());
            sentencia.setInt(3, reporte.getIdProgramaEducativo());
            sentencia.setInt(4, reporte.getNumSesion());
            sentencia.setString(5, reporte.getEstatus());
            sentencia.setString(6, reporte.getComentarios());
            sentencia.setInt(7, reporte.getNumAlumnosAsistieron());
            sentencia.setInt(8, reporte.getNumAlumnosRiesgo());
            sentencia.setString(9, reporte.getFechaPrimeraTutoria()); 
            sentencia.setString(10, reporte.getFechaUltimaTutoria());
            sentencia.setString(11, "Sin observaciones");
            
            int filas = sentencia.executeUpdate();
            
            if (filas > 0) {
                try (ResultSet resultado = sentencia.getGeneratedKeys()) {
                    if (resultado.next()) {
                        idGenerado = resultado.getInt(1);
                    }
                }
            }
        }
        return idGenerado;
    }

    public static int actualizarReporte(Connection conexion, ReporteTutoria reporte) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        int filas = 0;
        
        String consulta = "UPDATE reporteTutoria SET comentarios = ?, estatus = ?, "
                   + "numAlumnosAsistieron = ?, numAlumnosRiesgo = ? "
                   + "WHERE idReporteTutoria = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, reporte.getComentarios());
            sentencia.setString(2, reporte.getEstatus());
            sentencia.setInt(3, reporte.getNumAlumnosAsistieron());
            sentencia.setInt(4, reporte.getNumAlumnosRiesgo());
            sentencia.setInt(5, reporte.getIdReporteTutoria());
            filas = sentencia.executeUpdate();
        }
     return filas;
    }
    
    public static ArrayList<ReporteTutoria> obtenerReportesPorPeriodo(Connection conexion, int idTutor, int idPeriodo) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        ArrayList<ReporteTutoria> lista = new ArrayList<>();
        String consulta = "SELECT * FROM reporteTutoria WHERE idTutor = ? AND idPeriodoEscolar = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, idPeriodo);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    ReporteTutoria reporte = new ReporteTutoria();
                    reporte.setIdReporteTutoria(resultado.getInt("idReporteTutoria"));
                    reporte.setNumSesion(resultado.getInt("numSesion"));
                    reporte.setEstatus(resultado.getString("estatus"));
                    reporte.setFechaUltimaTutoria(resultado.getString("fechaUltimaTutoria"));
                    lista.add(reporte);
                }
            }
        }
        return lista;
    }
}