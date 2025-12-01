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

    public static ArrayList<FechaTutoria> obtenerFechasPorPeriodo(Connection conexion, int idPeriodo) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        ArrayList<FechaTutoria> fechas = new ArrayList<>();
        
        String consulta = "SELECT * FROM fechaTutoria WHERE idPeriodo = ? ORDER BY numSesion ASC";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idPeriodo);
            try (ResultSet rs = sentencia.executeQuery()) {
                while(rs.next()){
                    FechaTutoria ft = new FechaTutoria();
                    ft.setIdFechaTutoria(rs.getInt("idFechaTutoria"));
                    ft.setIdPeriodo(rs.getInt("idPeriodo"));
                    ft.setNumSesion(rs.getInt("numSesion"));
                    ft.setFechaInicio(rs.getString("fechaInicio"));
                    ft.setFechaCierre(rs.getString("fechaCierre"));
                    fechas.add(ft);
                }
            }
        }
        
        return fechas;
    }
    
    public static ArrayList<Integer> obtenerSesionesOcupadas(Connection conexion, int idTutor, int idPeriodo) throws SQLException {
        ArrayList<Integer> ocupadas = new ArrayList<>();
        
        if(conexion != null){
            String consulta = "SELECT DISTINCT numSesion FROM sesionTutoria WHERE idTutor = ? AND idPeriodo = ?";
            
            try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
                sentencia.setInt(1, idTutor);
                sentencia.setInt(2, idPeriodo);
                try (ResultSet rs = sentencia.executeQuery()) {
                    while(rs.next()){
                        ocupadas.add(rs.getInt("numSesion"));
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
        
        String consulta = "SELECT s.numSesion, p.nombre AS nombrePeriodo, MIN(s.fecha) AS fecha " +
                "FROM sesionTutoria s " +
                "INNER JOIN periodoEscolar p ON s.idPeriodo = p.idPeriodo " +
                "WHERE s.idTutor = ? " +
                "GROUP BY s.numSesion, p.nombre " +
                "ORDER BY s.numSesion ASC";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            try (ResultSet rs = sentencia.executeQuery()) {
                while(rs.next()){
                    SesionTutoria sesion = new SesionTutoria();
                    sesion.setNumSesion(rs.getInt("numSesion"));
                    sesion.setPeriodo(rs.getString("nombrePeriodo"));
                    sesion.setFecha(rs.getString("fecha"));
                    lista.add(sesion);
                }
            }
        }
        
        return lista;
    }

    public static ArrayList<SesionTutoria> obtenerAlumnosPorSesion(Connection conexion, int idTutor, int numSesion) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        ArrayList<SesionTutoria> lista = new ArrayList<>();
        
        String consulta = "SELECT st.idSesion, st.matriculaEstudiante, st.estado, " +
                "e.nombre, e.apellidoPaterno, e.apellidoMaterno, c.nombre AS carrera " +
                "FROM sesionTutoria st " +
                "INNER JOIN estudiante e ON st.matriculaEstudiante = e.matricula " +
                "INNER JOIN carrera c ON e.idCarrera = c.idCarrera " +
                "WHERE st.idTutor = ? AND st.numSesion = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, numSesion);
            
            try (ResultSet rs = sentencia.executeQuery()) {
                while(rs.next()){
                    SesionTutoria fila = new SesionTutoria();
                    fila.setIdSesion(rs.getInt("idSesion"));
                    fila.setMatriculaEstudiante(rs.getString("matriculaEstudiante"));
                    fila.setNombreEstudiante(rs.getString("nombre") + " " +
                            rs.getString("apellidoPaterno") + " " +
                            rs.getString("apellidoMaterno"));
                    fila.setEstado(rs.getString("estado"));
                    lista.add(fila);
                }
            }
            
        }
        
        return lista;
    }
    
    public static int registrarSesion(Connection conexion, SesionTutoria sesion) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        int filas = 0;
        
        String consulta = "INSERT INTO sesionTutoria "
                + "(idPeriodo, idTutor, matriculaEstudiante, numSesion, fecha, horaInicio, horaFin, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, sesion.getIdPeriodo());
            sentencia.setInt(2, sesion.getIdTutor());
            sentencia.setString(3, sesion.getMatriculaEstudiante());
            sentencia.setInt(4, sesion.getNumSesion());
            sentencia.setString(5, sesion.getFecha());
            sentencia.setString(6, sesion.getHoraInicio());
            sentencia.setString(7, sesion.getHoraFin());
            sentencia.setString(8, "Programada");
            filas = sentencia.executeUpdate();
        }
        
        return filas;
    }
}