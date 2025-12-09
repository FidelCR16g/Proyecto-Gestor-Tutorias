package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.FechaTutoria;
import gestortutoriasfx.modelo.pojo.SesionTutoria;
import java.sql.Connection;
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
    public static int actualizarEstadoAsistencia(Connection conexion, int idSesion, boolean asistio) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        int filasAfectadas = 0;
        String estado = asistio ? "Asistio" : "No Asistio";
        String consulta = "UPDATE sesionTutoria SET estado = ? WHERE idSesion = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, estado);
            sentencia.setInt(2, idSesion);
            filasAfectadas = sentencia.executeUpdate();
        
        }
        return filasAfectadas;
    }
    
    public static ArrayList<SesionTutoria> obtenerAlumnosPorSesion(Connection conexion, int idTutor, int numSesion) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        ArrayList<SesionTutoria> lista = new ArrayList<>();
        
        String consulta = "SELECT * FROM sesionTutoria WHERE idTutor = ? AND numSesion = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, numSesion);
            try (ResultSet rs = sentencia.executeQuery()) {
                while(rs.next()){
                    SesionTutoria fila = new SesionTutoria();
                    fila.setIdSesion(rs.getInt("idSesion"));
                    fila.setMatriculaEstudiante(rs.getString("matriculaEstudiante"));
                    fila.setNombreEstudiante(rs.getString("nombreEstudiante"));
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
        
        String consulta = "SELECT * FROM fechaTutoria WHERE idPeriodoEscolar = ? ORDER BY numSesion ASC";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idPeriodo);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while(resultado.next()){
                    FechaTutoria fechaTutoria = new FechaTutoria();
                    fechaTutoria.setIdFechaTutoria(resultado.getInt("idFechaTutoria"));
                    fechaTutoria.setIdPeriodoEscolar(resultado.getInt("idPeriodoEscolar"));
                    fechaTutoria.setNumSesion(resultado.getInt("numSesion"));
                    fechaTutoria.setFechaInicio(resultado.getString("fechaInicio"));
                    fechaTutoria.setFechaCierre(resultado.getString("fechaCierre"));
                    fechas.add(fechaTutoria);
                }
            }
        }
        
        return fechas;
    }
    
    public static ArrayList<Integer> obtenerSesionesOcupadas(Connection conexion, int idTutor, int idPeriodo) throws SQLException {
        ArrayList<Integer> ocupadas = new ArrayList<>();
        
        if(conexion != null){
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
        } else {
            throw new SQLException("No hay conexión con la base de datos.");
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
                    sesion.setFecha(resultado.getString("fecha"));
                    lista.add(sesion);
                }
            }
        }
        
        return lista;
    }
    
    public static int registrarSesion(Connection conexion, SesionTutoria sesion) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        int filas = 0;
        
        String consulta = "INSERT INTO sesionTutoria "
                + "(idPeriodoEscolar, idTutor, matriculaEstudiante, numSesion, fecha, horaInicio, horaFin, estado, idSalon, modalidad) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, sesion.getIdPeriodoEscolar());
            sentencia.setInt(2, sesion.getIdTutor());
            sentencia.setString(3, sesion.getMatriculaEstudiante());
            sentencia.setInt(4, sesion.getNumSesion());
            sentencia.setString(5, sesion.getFecha());
            sentencia.setString(6, sesion.getHoraInicio());
            sentencia.setString(7, sesion.getHoraFin());
            sentencia.setString(8, "Programada");
            sentencia.setInt(9, sesion.getIdSalon());
            sentencia.setString(10, sesion.getModalidad());
            filas = sentencia.executeUpdate();
        }
        
        return filas;
    }
}