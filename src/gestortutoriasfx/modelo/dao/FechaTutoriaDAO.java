package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.FechaTutoria;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FechaTutoriaDAO {

    public static ArrayList<FechaTutoria> obtenerFechasPorPeriodo(Connection conexion, int idPeriodoEscolar) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<FechaTutoria> lista = new ArrayList<>();
        String consulta = "SELECT idFechaTutoria, idPeriodoEscolar, descripcion, numSesion, fechaInicio, fechaCierre " +
                          "FROM fechaTutoria WHERE idPeriodoEscolar = ? ORDER BY numSesion ASC";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idPeriodoEscolar);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearFecha(rs));
                }
            }
        }
        return lista;
    }

    public static FechaTutoria obtenerPorPeriodoYNumSesion(Connection conexion, int idPeriodo, int numSesion) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        FechaTutoria fechaEncontrada = null;
        String consulta = "SELECT idFechaTutoria, idPeriodoEscolar, descripcion, numSesion, fechaInicio, fechaCierre " +
                          "FROM fechaTutoria WHERE idPeriodoEscolar = ? AND numSesion = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idPeriodo);
            sentencia.setInt(2, numSesion);
            
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    fechaEncontrada = mapearFecha(rs);
                }
            }
        }
        return fechaEncontrada;
    }

    public static Set<LocalDate> obtenerFechasOcupadas(Connection conexion, int idPeriodoEscolar) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        Set<LocalDate> ocupadas = new HashSet<>();
        String consulta = "SELECT fechaInicio FROM fechaTutoria WHERE idPeriodoEscolar = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, idPeriodoEscolar);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Date sqlDate = rs.getDate("fechaInicio");
                    if (sqlDate != null) {
                        ocupadas.add(sqlDate.toLocalDate());
                    }
                }
            }
        }
        return ocupadas;
    }

    public static int registrarFechaTutoria(Connection conexion, FechaTutoria fecha) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        String consulta = "INSERT INTO fechaTutoria (idPeriodoEscolar, descripcion, numSesion, fechaInicio, fechaCierre) " +
                          "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setInt(1, fecha.getIdPeriodoEscolar());
            sentencia.setString(2, fecha.getDescripcion());
            sentencia.setInt(3, fecha.getNumSesion());
            sentencia.setString(4, fecha.getFecha());
            sentencia.setString(5, fecha.getFechaCierre()); 
            
            return sentencia.executeUpdate();
        }
    }

    public static int actualizarFechaTutoria(Connection conexion, FechaTutoria fecha) throws SQLException {
        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        String consulta = "UPDATE fechaTutoria SET descripcion = ?, fechaInicio = ?, fechaCierre = ? " +
                          "WHERE idFechaTutoria = ?";

        try (PreparedStatement sentencia = conexion.prepareStatement(consulta)) {
            sentencia.setString(1, fecha.getDescripcion());
            sentencia.setString(2, fecha.getFecha());
            sentencia.setString(3, fecha.getFechaCierre());
            sentencia.setInt(4, fecha.getIdFechaTutoria());
            
            return sentencia.executeUpdate();
        }
    }

    private static FechaTutoria mapearFecha(ResultSet rs) throws SQLException {
        FechaTutoria ft = new FechaTutoria();
        ft.setIdFechaTutoria(rs.getInt("idFechaTutoria"));
        ft.setIdPeriodoEscolar(rs.getInt("idPeriodoEscolar"));
        ft.setDescripcion(rs.getString("descripcion"));
        ft.setNumSesion(rs.getInt("numSesion"));
        
        Date fechaSql = rs.getDate("fechaInicio");
        if (fechaSql != null) {
            ft.setFecha(fechaSql.toString());
        }
        
        Date fechaCierreSql = rs.getDate("fechaCierre");
        if (fechaCierreSql != null) {
            ft.setFechaCierre(fechaCierreSql.toString());
        }
        return ft;
    }
}