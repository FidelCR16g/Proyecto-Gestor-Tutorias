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

    public static ArrayList<Estudiante> obtenerTutoradosPorTutor(Connection conexion, int idTutor) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<Estudiante> listaEstudiantes = new ArrayList<>();

        String consulta =
                "SELECT e.*, pe.nombre AS nombrePrograma " +
                "FROM estudiante e " +
                "INNER JOIN asignacionTutorado a ON e.idEstudiante = a.idEstudiante " +
                "INNER JOIN programaEducativo pe ON e.idProgramaEducativo = pe.idProgramaEducativo " +
                "WHERE a.idTutor = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    Estudiante estudiante = new Estudiante();
                    estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
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
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<Estudiante> estudiantes = new ArrayList<>();

        String consulta =
                "SELECT e.*, pe.nombre AS nombrePrograma " +
                "FROM estudiante e " +
                "INNER JOIN programaEducativo pe ON e.idProgramaEducativo = pe.idProgramaEducativo " +
                "LEFT JOIN asignacionTutorado a ON e.idEstudiante = a.idEstudiante " +
                "WHERE a.idAsignacionTutorado IS NULL " +
                "ORDER BY e.semestre, e.apellidoPaterno";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setIdEstudiante(resultado.getInt("idEstudiante"));
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
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        Integer idEstudiante = obtenerIdEstudiantePorMatricula(conexion, matricula);
        if (idEstudiante == null) throw new SQLException("No existe estudiante con matrícula: " + matricula);

        String consulta = "INSERT INTO asignacionTutorado (idTutor, idEstudiante) VALUES (?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, idEstudiante);
            return sentencia.executeUpdate();
        }
    }

    public static int desasignarTutor(Connection conexion, String matricula, int idTutor) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        Integer idEstudiante = obtenerIdEstudiantePorMatricula(conexion, matricula);
        if (idEstudiante == null) throw new SQLException("No existe estudiante con matrícula: " + matricula);

        String consulta = "DELETE FROM asignacionTutorado WHERE idTutor = ? AND idEstudiante = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idTutor);
            sentencia.setInt(2, idEstudiante);
            return sentencia.executeUpdate();
        }
    }

    public static ArrayList<Estudiante> obtenerTodos(Connection conexion) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<Estudiante> lista = new ArrayList<>();

        String consulta =
                "SELECT e.*, pe.nombre AS nombrePrograma " +
                "FROM estudiante e " +
                "INNER JOIN programaEducativo pe ON e.idProgramaEducativo = pe.idProgramaEducativo " +
                "ORDER BY e.apellidoPaterno, e.apellidoMaterno, e.nombre";

        try (PreparedStatement ps = conexion.prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Estudiante est = new Estudiante();
                est.setIdEstudiante(rs.getInt("idEstudiante"));
                est.setMatricula(rs.getString("matricula"));
                est.setNombre(rs.getString("nombre"));
                est.setApellidoPaterno(rs.getString("apellidoPaterno"));
                est.setApellidoMaterno(rs.getString("apellidoMaterno"));
                est.setTelefono(rs.getString("telefono"));
                est.setCorreoInstitucional(rs.getString("correoInstitucional"));
                est.setAnioIngreso(rs.getInt("anioIngreso"));
                est.setSemestre(rs.getInt("semestre"));
                est.setCreditosObtenidos(rs.getInt("creditosObtenidos"));
                est.setSituacionRiesgo(rs.getBoolean("situacionRiesgo"));
                est.setCambioTutor(rs.getInt("cambioTutor"));
                est.setPerfilActivo(rs.getBoolean("perfilActivo"));
                est.setIdProgramaEducativo(rs.getInt("idProgramaEducativo"));
                est.setNombreProgramaEducativo(rs.getString("nombrePrograma"));
                lista.add(est);
            }
        }

        return lista;
    }

    public static int eliminarPorMatricula(Connection conexion, String matricula) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        Integer idEstudiante = obtenerIdEstudiantePorMatricula(conexion, matricula);
        if (idEstudiante == null) throw new SQLException("No existe estudiante con matrícula: " + matricula);

        int total = 0;

        String q1 = "DELETE FROM asignacionTutorado WHERE idEstudiante = ?";
        try (PreparedStatement ps = conexion.prepareStatement(q1)) {
            ps.setInt(1, idEstudiante);
            total += ps.executeUpdate();
        }

        String q2 = "DELETE FROM estudiante WHERE idEstudiante = ?";
        try (PreparedStatement ps = conexion.prepareStatement(q2)) {
            ps.setInt(1, idEstudiante);
            total += ps.executeUpdate();
        }

        return total;
    }

    public static int actualizar(Connection conexion, Estudiante e) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        String q = "UPDATE estudiante SET nombre=?, apellidoPaterno=?, apellidoMaterno=?, telefono=?, " +
                   "correoInstitucional=?, anioIngreso=?, semestre=?, perfilActivo=? " +
                   "WHERE matricula=?";

        try (PreparedStatement ps = conexion.prepareStatement(q)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellidoPaterno());
            ps.setString(3, e.getApellidoMaterno());
            ps.setString(4, e.getTelefono());
            ps.setString(5, e.getCorreoInstitucional());
            ps.setInt(6, e.getAnioIngreso());
            ps.setInt(7, e.getSemestre());
            ps.setBoolean(8, e.isPerfilActivo());
            ps.setString(9, e.getMatricula());
            return ps.executeUpdate();
        }
    }

    public static int actualizarFoto(Connection conexion, String matricula, byte[] foto) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        String q = "UPDATE estudiante SET foto=? WHERE matricula=?";
        try (PreparedStatement ps = conexion.prepareStatement(q)) {
            ps.setBytes(1, foto);
            ps.setString(2, matricula);
            return ps.executeUpdate();
        }
    }

    public static Estudiante obtenerPorMatricula(Connection conexion, String matricula) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        String q =
            "SELECT e.*, pe.nombre AS nombrePrograma, at.idTutor AS idTutorAsignado " +
            "FROM estudiante e " +
            "INNER JOIN programaEducativo pe ON e.idProgramaEducativo = pe.idProgramaEducativo " +
            "LEFT JOIN asignacionTutorado at ON at.idEstudiante = e.idEstudiante " +
            "WHERE e.matricula = ?";

        try (PreparedStatement ps = conexion.prepareStatement(q)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Estudiante est = new Estudiante();
                    est.setIdEstudiante(rs.getInt("idEstudiante"));
                    est.setMatricula(rs.getString("matricula"));
                    est.setNombre(rs.getString("nombre"));
                    est.setApellidoPaterno(rs.getString("apellidoPaterno"));
                    est.setApellidoMaterno(rs.getString("apellidoMaterno"));
                    est.setTelefono(rs.getString("telefono"));
                    est.setCorreoInstitucional(rs.getString("correoInstitucional"));
                    est.setAnioIngreso(rs.getInt("anioIngreso"));
                    est.setSemestre(rs.getInt("semestre"));
                    est.setCreditosObtenidos(rs.getInt("creditosObtenidos"));
                    est.setSituacionRiesgo(rs.getBoolean("situacionRiesgo"));
                    est.setCambioTutor(rs.getInt("cambioTutor"));
                    est.setPerfilActivo(rs.getBoolean("perfilActivo"));
                    est.setIdProgramaEducativo(rs.getInt("idProgramaEducativo"));
                    est.setNombreProgramaEducativo(rs.getString("nombrePrograma"));
                    est.setFoto(rs.getBytes("foto"));
                    int idTutor = rs.getInt("idTutorAsignado");
                    est.setIdTutor(rs.wasNull() ? null : idTutor);

                    return est;
                }
            }
        }
        return null;
    }
 
    public static int insertar(Connection con, Estudiante e) throws SQLException {
        if (con == null) throw new SQLException("No hay conexión con la base de datos.");

        String q = "INSERT INTO estudiante " +
                "(matricula, nombre, apellidoPaterno, apellidoMaterno, telefono, correoInstitucional, " +
                "anioIngreso, semestre, perfilActivo, idProgramaEducativo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(q, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getMatricula());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getApellidoPaterno());
            ps.setString(4, e.getApellidoMaterno());
            ps.setString(5, e.getTelefono());
            ps.setString(6, e.getCorreoInstitucional());
            ps.setInt(7, e.getAnioIngreso());
            ps.setInt(8, e.getSemestre());
            ps.setBoolean(9, e.isPerfilActivo());
            ps.setInt(10, e.getIdProgramaEducativo());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        throw new SQLException("No se pudo obtener el idEstudiante generado.");
    }   
    
    public static int insertarAsignacionTutorado(Connection con, int idTutor, int idEstudiante) throws SQLException {
        if (con == null) throw new SQLException("No hay conexión con la base de datos.");

        String q = "INSERT INTO asignacionTutorado (idTutor, idEstudiante) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(q)) {
            ps.setInt(1, idTutor);
            ps.setInt(2, idEstudiante);
            return ps.executeUpdate();
        }
}

    
}

