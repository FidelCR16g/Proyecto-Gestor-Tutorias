package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.FechaTutoria;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
    private static Integer obtenerIdEstudiantePorMatricula(Connection conexion, String matricula) throws SQLException {
        String q = "SELECT idEstudiante FROM estudiante WHERE matricula = ?";
        try (PreparedStatement ps = conexion.prepareStatement(q)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("idEstudiante");
            }
        }
        return null;
    }

    public static int actualizarEstadoAsistencia(Connection conexion, int idSesion, boolean asistio) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        String estado = asistio ? "Asistio" : "No Asistio";
        String consulta = "UPDATE sesionTutoria SET estado = ? WHERE idSesion = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, estado);
            sentencia.setInt(2, idSesion);
            return sentencia.executeUpdate();
        }
    }

    public static ArrayList<SesionTutoria> obtenerAlumnosPorSesion(Connection conexion, int idTutor, int numSesion) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<SesionTutoria> lista = new ArrayList<>();
        
        String consulta =
                "SELECT s.idSesion, s.idEstudiante, e.matricula, " +
                "CONCAT(e.nombre, ' ', e.apellidoPaterno, ' ', e.apellidoMaterno) AS nombreCompleto, " +
                "s.estado " +
                "FROM sesionTutoria s " +
                "LEFT JOIN estudiante e ON s.idEstudiante = e.idEstudiante " +
                "WHERE s.idTutor = ? AND s.numSesion = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, numSesion);

            try (ResultSet rs = sentencia.executeQuery()) {
                while(rs.next()){
                    SesionTutoria fila = new SesionTutoria();
                    fila.setIdSesion(rs.getInt("idSesion"));
                    
                    int idEst = rs.getInt("idEstudiante");
                    fila.setIdEstudiante(rs.wasNull() ? null : idEst);
                    
                    fila.setMatriculaEstudiante(rs.getString("matricula"));
                    fila.setNombreEstudiante(rs.getString("nombreCompleto"));
                    fila.setEstado(rs.getString("estado"));
                    
                    lista.add(fila);
                }
            }
        }
        return lista;
    }

    public static ArrayList<FechaTutoria> obtenerFechasPorPeriodo(Connection conexion, int idPeriodo) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<FechaTutoria> fechas = new ArrayList<>();
        
        String consulta = "SELECT idFechaTutoria, idPeriodoEscolar, numSesion, descripcion, fechaInicio, fechaCierre " +
                          "FROM fechaTutoria WHERE idPeriodoEscolar = ? ORDER BY numSesion ASC";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idPeriodo);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while(resultado.next()){
                    FechaTutoria ft = new FechaTutoria();
                    ft.setIdFechaTutoria(resultado.getInt("idFechaTutoria"));
                    ft.setIdPeriodoEscolar(resultado.getInt("idPeriodoEscolar"));
                    ft.setNumSesion(resultado.getInt("numSesion"));
                    ft.setDescripcion(resultado.getString("descripcion"));
                    
                    java.sql.Date fechaInicio = resultado.getDate("fechaInicio");
                    ft.setFecha(fechaInicio != null ? fechaInicio.toString() : null);
                    
                    java.sql.Date fechaCierre = resultado.getDate("fechaCierre");
                    ft.setFechaCierre(fechaCierre != null ? fechaCierre.toString() : null);
                    
                    fechas.add(ft);
                }
            }
        }
        return fechas; 
    }

    public static ArrayList<Integer> obtenerSesionesOcupadas(Connection conexion, int idTutor, int idPeriodo) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<Integer> ocupadas = new ArrayList<>();
        String consulta = "SELECT DISTINCT numSesion FROM sesionTutoria WHERE idTutor = ? AND idPeriodoEscolar = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, idPeriodo);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while(resultado.next()){
                    ocupadas.add(resultado.getInt("numSesion"));
                }
            }
        }
        return ocupadas;
    }

    public static ArrayList<SesionTutoria> obtenerSesionesAgrupadasPorTutor(Connection conexion, int idTutor) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<SesionTutoria> lista = new ArrayList<>();

        String consulta = "SELECT s.numSesion, p.nombrePeriodoEscolar AS nombrePeriodo, MIN(s.fecha) AS fecha " +
                "FROM sesionTutoria s " +
                "INNER JOIN periodoEscolar p ON s.idPeriodoEscolar = p.idPeriodoEscolar " +
                "WHERE s.idTutor = ? " +
                "GROUP BY s.numSesion, p.nombrePeriodoEscolar " +
                "ORDER BY s.numSesion ASC";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while(resultado.next()){
                    SesionTutoria sesion = new SesionTutoria();
                    sesion.setNumSesion(resultado.getInt("numSesion"));
                    sesion.setPeriodo(resultado.getString("nombrePeriodo"));
                    
                    java.sql.Date fechaSql = resultado.getDate("fecha");
                    sesion.setFecha(fechaSql != null ? fechaSql.toString() : "");
                    
                    lista.add(sesion);
                }
            }
        }
        return lista;
    }

    public static int registrarSesion(Connection conexion, SesionTutoria sesion) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        Integer idEst = sesion.getIdEstudiante();

        if ((idEst == null || idEst <= 0) && sesion.getMatriculaEstudiante() != null) {
            idEst = obtenerIdEstudiantePorMatricula(conexion, sesion.getMatriculaEstudiante());
        }
        
        if (idEst == null) {
            throw new SQLException("No se pudo determinar el estudiante (matricula no encontrada: " 
                + sesion.getMatriculaEstudiante() + ")");
        }

        String consulta = "INSERT INTO sesionTutoria "
                + "(idPeriodoEscolar, idTutor, idEstudiante, numSesion, fecha, horaInicio, horaFin, estado, idSalon, modalidad) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, sesion.getIdPeriodoEscolar());
            sentencia.setInt(2, sesion.getIdTutor());
            sentencia.setInt(3, idEst);
            sentencia.setInt(4, sesion.getNumSesion());
            sentencia.setString(5, sesion.getFecha());
            sentencia.setString(6, sesion.getHoraInicio());
            sentencia.setString(7, sesion.getHoraFin());
            
            sentencia.setString(8, "Programada");
            sentencia.setInt(9, sesion.getIdSalon());
            sentencia.setString(10, sesion.getModalidad());
            
            return sentencia.executeUpdate();
        }
    }
}