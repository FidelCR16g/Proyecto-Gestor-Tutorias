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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReporteTutoriaDAO {

    public static ReporteTutoria obtenerReporte(Connection conexion, int idTutor, int idPeriodo, int numSesion) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ReporteTutoria reporte = null;
        String consulta = "SELECT rt.*, " +
                          "pe.nombre AS nombrePrograma, " +
                          "per.nombrePeriodoEscolar, " +
                          "CONCAT(p.nombre, ' ', p.apellidoPaterno) AS nombreTutor " +
                          "FROM reporteTutoria rt " +
                          "INNER JOIN programaEducativo pe ON rt.idProgramaEducativo = pe.idProgramaEducativo " +
                          "INNER JOIN periodoEscolar per ON rt.idPeriodoEscolar = per.idPeriodoEscolar " +
                          "INNER JOIN tutor t ON rt.idTutor = t.idTutor " +
                          "INNER JOIN profesor p ON t.idProfesor = p.idProfesor " +
                          "WHERE rt.idTutor = ? AND rt.idPeriodoEscolar = ? AND rt.numSesion = ?";

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
                    
                    reporte.setFechaPrimeraTutoria(resultado.getString("fechaPrimeraTutoria"));
                    reporte.setFechaUltimaTutoria(resultado.getString("fechaUltimaTutoria"));

                    reporte.setNombreTutor(resultado.getString("nombreTutor"));
                    reporte.setNombreProgramaEducativo(resultado.getString("nombrePrograma"));
                    reporte.setNombrePeriodoEscolar(resultado.getString("nombrePeriodoEscolar"));
                }
            }
        }
        return reporte;
    }

    public static int registrarReporte(Connection conexion, ReporteTutoria reporte) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

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
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        int filas = 0;

        String consulta = "UPDATE reporteTutoria SET comentarios = ?, estatus = ?, "
                        + "numAlumnosAsistieron = ?, numAlumnosRiesgo = ?, "
                        + "fechaPrimeraTutoria = ?, fechaUltimaTutoria = ? "
                        + "WHERE idReporteTutoria = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, reporte.getComentarios());
            sentencia.setString(2, reporte.getEstatus());
            sentencia.setInt(3, reporte.getNumAlumnosAsistieron());
            sentencia.setInt(4, reporte.getNumAlumnosRiesgo());
            sentencia.setString(5, reporte.getFechaPrimeraTutoria());
            sentencia.setString(6, reporte.getFechaUltimaTutoria());
            
            sentencia.setInt(7, reporte.getIdReporteTutoria());
            
            filas = sentencia.executeUpdate();
        }
        return filas;
    }

    public static ArrayList<ReporteTutoria> obtenerReportesPorPeriodo(Connection conexion, int idTutor, int idPeriodo) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<ReporteTutoria> lista = new ArrayList<>();
        
        String consulta = "SELECT * FROM reporteTutoria WHERE idTutor = ? AND idPeriodoEscolar = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, idPeriodo);
            
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    ReporteTutoria reporte = new ReporteTutoria();
                    reporte.setIdReporteTutoria(resultado.getInt("idReporteTutoria"));
                    reporte.setIdTutor(resultado.getInt("idTutor"));
                    reporte.setIdPeriodoEscolar(resultado.getInt("idPeriodoEscolar"));
                    reporte.setIdProgramaEducativo(resultado.getInt("idProgramaEducativo"));
                    reporte.setNumSesion(resultado.getInt("numSesion"));
                    reporte.setEstatus(resultado.getString("estatus"));
                    reporte.setFechaUltimaTutoria(resultado.getString("fechaUltimaTutoria"));
                    
                    lista.add(reporte);
                }
            }
        }
        return lista;
    }

    public static ArrayList<ReporteTutoria> obtenerReportesParaRevision(Connection c, String busqueda) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<ReporteTutoria> lista = new ArrayList<>();
        String q = (busqueda == null) ? "" : busqueda.trim();
        String like = "%" + q + "%";

        String sql =
            "SELECT rt.idReporteTutoria, rt.idTutor, rt.idPeriodoEscolar, rt.idProgramaEducativo, " +
            "       rt.numSesion, rt.estatus, rt.fechaUltimaTutoria, rt.fechaCreacionReporte, " +
            "       CONCAT(p.nombre, ' ', p.apellidoPaterno) AS nombreTutor, " +
            "       pe.nombre AS nombreProgramaEducativo, " +
            "       per.nombrePeriodoEscolar " +
            "FROM reporteTutoria rt " +
            "INNER JOIN tutor t ON rt.idTutor = t.idTutor " +
            "INNER JOIN profesor p ON t.idProfesor = p.idProfesor " +
            "INNER JOIN programaEducativo pe ON rt.idProgramaEducativo = pe.idProgramaEducativo " +
            "INNER JOIN periodoEscolar per ON rt.idPeriodoEscolar = per.idPeriodoEscolar " +
            "WHERE (rt.estatus = 'Enviado' OR rt.estatus = 'Revisado') " +
            "  AND ( ? = '' " +
            "        OR CONCAT(p.nombre, ' ', p.apellidoPaterno) LIKE ? " +
            "        OR pe.nombre LIKE ? " +
            "        OR per.nombrePeriodoEscolar LIKE ? " +
            "        OR CAST(rt.numSesion AS CHAR) LIKE ? " +
            "        OR rt.estatus LIKE ? " +
            "        OR CAST(rt.fechaUltimaTutoria AS CHAR) LIKE ? " +
            "        OR CAST(rt.fechaCreacionReporte AS CHAR) LIKE ? ) " +
            "ORDER BY rt.fechaCreacionReporte DESC, rt.numSesion DESC";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, q);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            ps.setString(5, like);
            ps.setString(6, like);
            ps.setString(7, like);
            ps.setString(8, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReporteTutoria r = new ReporteTutoria();
                    r.setIdReporteTutoria(rs.getInt("idReporteTutoria"));
                    r.setIdTutor(rs.getInt("idTutor"));
                    r.setIdPeriodoEscolar(rs.getInt("idPeriodoEscolar"));
                    r.setIdProgramaEducativo(rs.getInt("idProgramaEducativo"));

                    r.setNumSesion(rs.getInt("numSesion"));
                    r.setEstatus(rs.getString("estatus"));

                    r.setNombreTutor(rs.getString("nombreTutor"));
                    r.setNombreProgramaEducativo(rs.getString("nombreProgramaEducativo"));
                    r.setNombrePeriodoEscolar(rs.getString("nombrePeriodoEscolar"));

                    r.setFechaUltimaTutoria(rs.getString("fechaUltimaTutoria"));
                    r.setFechaCreacionReporte(rs.getString("fechaCreacionReporte"));

                    lista.add(r);
                }
            }
        }
        return lista;
    }

    public static ReporteTutoria obtenerReportePorId(Connection c, int idReporteTutoria) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        ReporteTutoria reporte = null;

        String sql =
            "SELECT rt.*, " +
            "       CONCAT(p.nombre, ' ', p.apellidoPaterno) AS nombreTutor, " +
            "       pe.nombre AS nombreProgramaEducativo, " +
            "       per.nombrePeriodoEscolar " +
            "FROM reporteTutoria rt " +
            "INNER JOIN tutor t ON rt.idTutor = t.idTutor " +
            "INNER JOIN profesor p ON t.idProfesor = p.idProfesor " +
            "INNER JOIN programaEducativo pe ON rt.idProgramaEducativo = pe.idProgramaEducativo " +
            "INNER JOIN periodoEscolar per ON rt.idPeriodoEscolar = per.idPeriodoEscolar " +
            "WHERE rt.idReporteTutoria = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idReporteTutoria);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    reporte = new ReporteTutoria();
                    reporte.setIdReporteTutoria(rs.getInt("idReporteTutoria"));
                    reporte.setIdTutor(rs.getInt("idTutor"));
                    reporte.setIdPeriodoEscolar(rs.getInt("idPeriodoEscolar"));
                    reporte.setIdProgramaEducativo(rs.getInt("idProgramaEducativo"));

                    reporte.setNumSesion(rs.getInt("numSesion"));
                    reporte.setEstatus(rs.getString("estatus"));
                    reporte.setComentarios(rs.getString("comentarios"));

                    reporte.setNumAlumnosAsistieron(rs.getInt("numAlumnosAsistieron"));
                    reporte.setNumAlumnosRiesgo(rs.getInt("numAlumnosRiesgo"));

                    reporte.setNombreTutor(rs.getString("nombreTutor"));
                    reporte.setNombreProgramaEducativo(rs.getString("nombreProgramaEducativo"));
                    reporte.setNombrePeriodoEscolar(rs.getString("nombrePeriodoEscolar"));

                    reporte.setFechaPrimeraTutoria(rs.getString("fechaPrimeraTutoria"));
                    reporte.setFechaUltimaTutoria(rs.getString("fechaUltimaTutoria"));

                    reporte.setEstadoLugar(rs.getString("estadoLugar"));
                    reporte.setFechaCreacionReporte(rs.getString("fechaCreacionReporte"));
                }
            }
        }
        return reporte;
    }

    public static int actualizarEstatus(Connection c, int idReporteTutoria, String estatus) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");
        if (estatus == null || estatus.trim().isEmpty()) throw new SQLException("Estatus inválido.");

        String sql = "UPDATE reporteTutoria SET estatus = ? WHERE idReporteTutoria = ? AND estatus = 'Enviado'";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, estatus);
            ps.setInt(2, idReporteTutoria);
            return ps.executeUpdate(); 
        }
    }
}