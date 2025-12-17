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

        int filasAfectadas;
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
        String consulta =
                "SELECT s.idSesion, s.idEstudiante, e.matricula, s.nombreEstudiante, s.estado " +
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
                    fila.setIdEstudiante((Integer) rs.getObject("idEstudiante"));
                    fila.setMatriculaEstudiante(rs.getString("matricula")); // ✅ compat
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
        String consulta = "SELECT idFechaTutoria, idPeriodoEscolar, numSesion, descripcion, fecha " +
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
                    ft.setFecha(resultado.getString("fecha")); 
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
                    sesion.setFecha(resultado.getString("fecha"));
                    lista.add(sesion);
                }
            }
        }
        return lista;
    }

    public static int registrarSesion(Connection conexion, SesionTutoria sesion) throws SQLException {
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        Integer idEst = sesion.getIdEstudiante();

        if (idEst == null && sesion.getMatriculaEstudiante() != null) {
            idEst = obtenerIdEstudiantePorMatricula(conexion, sesion.getMatriculaEstudiante());
        }
        if (idEst == null) throw new SQLException("No se pudo determinar idEstudiante (ni por id ni por matrícula).");

        int filas;

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
            filas = sentencia.executeUpdate();
        }
        return filas;
    }
}
