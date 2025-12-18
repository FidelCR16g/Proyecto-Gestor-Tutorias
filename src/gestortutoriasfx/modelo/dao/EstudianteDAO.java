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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EstudianteDAO {
    public static ArrayList<Estudiante> obtenerTutoradosPorTutor(Connection conexion, int idTutor) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<Estudiante> listaEstudiantes = new ArrayList<>();

        String consulta = 
            "SELECT e.*, pe.nombre AS nombrePrograma " +
            "FROM estudiante e " +
            "INNER JOIN asignacionTutorado a ON e.idEstudiante = a.idEstudiante " +
            "INNER JOIN programaEducativo pe ON e.idProgramaEducativo = pe.idProgramaEducativo " +
            "WHERE a.idTutor = ? AND e.perfilActivo = true";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    listaEstudiantes.add(mapearEstudiante(resultado));
                }
            }
        }
        return listaEstudiantes;
    }

    public static ArrayList<Estudiante> obtenerEstudiantesSinTutor(Connection conexion) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        ArrayList<Estudiante> estudiantes = new ArrayList<>();

        String consulta = 
            "SELECT e.*, pe.nombre AS nombrePrograma " +
            "FROM estudiante e " +
            "INNER JOIN programaEducativo pe ON e.idProgramaEducativo = pe.idProgramaEducativo " +
            "LEFT JOIN asignacionTutorado a ON e.idEstudiante = a.idEstudiante " +
            "WHERE a.idAsignacionTutorado IS NULL AND e.perfilActivo = true " +
            "ORDER BY e.semestre, e.apellidoPaterno";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                estudiantes.add(mapearEstudiante(resultado));
            }
        }
        return estudiantes;
    }

    public static int asignarTutor(Connection conexion, String matricula, int idTutor) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        Integer idEstudiante = obtenerIdPorMatricula(conexion, matricula);
        
        if (idEstudiante == null) {
            return 0; 
        }

        String consulta = "INSERT INTO asignacionTutorado (idTutor, idEstudiante) VALUES (?, ?)";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, idEstudiante);
            return sentencia.executeUpdate();
        }
    }

    public static int desasignarTutor(Connection conexion, String matricula, int idTutor) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        Integer idEstudiante = obtenerIdPorMatricula(conexion, matricula);
        
        if (idEstudiante == null) return 0;

        String consulta = "DELETE FROM asignacionTutorado WHERE idTutor = ? AND idEstudiante = ?";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, idEstudiante);
            return sentencia.executeUpdate();
        }
    }

    public static int insertar(Connection con, Estudiante e) throws SQLException {
        String q = "INSERT INTO estudiante " +
                "(matricula, idProgramaEducativo, nombre, apellidoPaterno, apellidoMaterno, telefono, " +
                "foto, correoInstitucional, anioIngreso, semestre, creditosObtenidos, situacionRiesgo, " +
                "cambioTutor, perfilActivo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(q, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getMatricula());
            ps.setInt(2, e.getIdProgramaEducativo());
            ps.setString(3, e.getNombre());
            ps.setString(4, e.getApellidoPaterno());
            ps.setString(5, e.getApellidoMaterno());
            ps.setString(6, e.getTelefono());
            ps.setBytes(7, e.getFoto() != null ? e.getFoto() : new byte[0]);
            ps.setString(8, e.getCorreoInstitucional());
            ps.setInt(9, e.getAnioIngreso());
            ps.setInt(10, e.getSemestre());
            ps.setInt(11, e.getCreditosObtenidos());
            ps.setBoolean(12, e.isSituacionRiesgo());
            ps.setInt(13, e.getCambioTutor());
            ps.setBoolean(14, e.isPerfilActivo());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("No se pudo obtener el ID generado.");
    }
    
    public static int actualizar(Connection conexion, Estudiante e) throws SQLException {
        String q = "UPDATE estudiante SET nombre=?, apellidoPaterno=?, apellidoMaterno=?, telefono=?, " +
                   "correoInstitucional=?, anioIngreso=?, semestre=?, creditosObtenidos=?, " +
                   "situacionRiesgo=?, cambioTutor=?, perfilActivo=? WHERE matricula=?";

        try (PreparedStatement ps = conexion.prepareStatement(q)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellidoPaterno());
            ps.setString(3, e.getApellidoMaterno());
            ps.setString(4, e.getTelefono());
            ps.setString(5, e.getCorreoInstitucional());
            ps.setInt(6, e.getAnioIngreso());
            ps.setInt(7, e.getSemestre());
            ps.setInt(8, e.getCreditosObtenidos());
            ps.setBoolean(9, e.isSituacionRiesgo());
            ps.setInt(10, e.getCambioTutor());
            ps.setBoolean(11, e.isPerfilActivo());
            ps.setString(12, e.getMatricula());
            return ps.executeUpdate();
        }
    }
    
    public static int actualizarFoto(Connection conexion, String matricula, byte[] foto) throws SQLException {
        String q = "UPDATE estudiante SET foto=? WHERE matricula=?";
        try (PreparedStatement ps = conexion.prepareStatement(q)) {
            ps.setBytes(1, foto != null ? foto : new byte[0]);
            ps.setString(2, matricula);
            return ps.executeUpdate();
        }
    }

    public static int eliminarPorMatricula(Connection conexion, String matricula) throws SQLException {
        String q = "UPDATE estudiante SET perfilActivo = false WHERE matricula = ?";
        try (PreparedStatement ps = conexion.prepareStatement(q)) {
            ps.setString(1, matricula);
            return ps.executeUpdate();
        }
    }
    
    public static Estudiante obtenerPorMatricula(Connection conexion, String matricula) throws SQLException {
        String q = "SELECT e.*, pe.nombre AS nombrePrograma, at.idTutor AS idTutorAsignado " +
                   "FROM estudiante e " +
                   "INNER JOIN programaEducativo pe ON e.idProgramaEducativo = pe.idProgramaEducativo " +
                   "LEFT JOIN asignacionTutorado at ON at.idEstudiante = e.idEstudiante " +
                   "WHERE e.matricula = ?";
        try (PreparedStatement ps = conexion.prepareStatement(q)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Estudiante est = mapearEstudiante(rs);
                    est.setFoto(rs.getBytes("foto")); // Cargamos la foto solo al buscar individualmente
                    int idTutor = rs.getInt("idTutorAsignado");
                    if (!rs.wasNull()) est.setIdTutor(idTutor);
                    return est;
                }
            }
        }
        return null;
    }
    
    public static ArrayList<Estudiante> obtenerTodos(Connection conexion) throws SQLException {
        ArrayList<Estudiante> lista = new ArrayList<>();
        String consulta = "SELECT e.*, pe.nombre AS nombrePrograma FROM estudiante e " +
                          "INNER JOIN programaEducativo pe ON e.idProgramaEducativo = pe.idProgramaEducativo " +
                          "WHERE e.perfilActivo = true ORDER BY e.apellidoPaterno";
        try (PreparedStatement ps = conexion.prepareStatement(consulta); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapearEstudiante(rs));
        }
        return lista;
    }

    private static Integer obtenerIdPorMatricula(Connection conexion, String matricula) throws SQLException {
        String q = "SELECT idEstudiante FROM estudiante WHERE matricula = ?";
        try (PreparedStatement ps = conexion.prepareStatement(q)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("idEstudiante");
            }
        }
        return null;
    }

    private static Estudiante mapearEstudiante(ResultSet rs) throws SQLException {
        Estudiante e = new Estudiante();
        e.setIdEstudiante(rs.getInt("idEstudiante"));
        e.setMatricula(rs.getString("matricula"));
        e.setNombre(rs.getString("nombre"));
        e.setApellidoPaterno(rs.getString("apellidoPaterno"));
        e.setApellidoMaterno(rs.getString("apellidoMaterno"));
        e.setCorreoInstitucional(rs.getString("correoInstitucional"));
        e.setSemestre(rs.getInt("semestre"));
        e.setIdProgramaEducativo(rs.getInt("idProgramaEducativo"));
        e.setNombreProgramaEducativo(rs.getString("nombrePrograma"));
        e.setSituacionRiesgo(rs.getBoolean("situacionRiesgo"));
        
        e.setTelefono(rs.getString("telefono"));
        e.setAnioIngreso(rs.getInt("anioIngreso"));
        e.setCreditosObtenidos(rs.getInt("creditosObtenidos"));
        e.setCambioTutor(rs.getInt("cambioTutor"));
        e.setPerfilActivo(rs.getBoolean("perfilActivo"));
        
        return e;
    }
}