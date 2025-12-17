package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.FechaTutoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FechaTutoriaDAO {

    public static List<FechaTutoria> obtenerPorPeriodo(Connection conexion, int idPeriodoEscolar)
            throws SQLException {

        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "SELECT * FROM fechaTutoria WHERE idPeriodoEscolar = ? ORDER BY numSesion ASC";
        List<FechaTutoria> lista = new ArrayList<>();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPeriodoEscolar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    FechaTutoria ft = new FechaTutoria();
                    ft.setIdFechaTutoria(rs.getInt("idFechaTutoria"));
                    ft.setIdPeriodoEscolar(rs.getInt("idPeriodoEscolar"));
                    ft.setNumSesion(rs.getInt("numSesion"));
                    ft.setDescripcion(rs.getString("descripcion"));
                    ft.setFecha(rs.getDate("fecha").toString()); // yyyy-MM-dd
                    lista.add(ft);
                }
            }
        }
        return lista;
    }

    public static FechaTutoria obtenerPorPeriodoYSesion(Connection conexion, int idPeriodoEscolar, int numSesion)
            throws SQLException {

        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "SELECT * FROM fechaTutoria WHERE idPeriodoEscolar = ? AND numSesion = ? LIMIT 1";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPeriodoEscolar);
            ps.setInt(2, numSesion);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FechaTutoria ft = new FechaTutoria();
                    ft.setIdFechaTutoria(rs.getInt("idFechaTutoria"));
                    ft.setIdPeriodoEscolar(rs.getInt("idPeriodoEscolar"));
                    ft.setNumSesion(rs.getInt("numSesion"));
                    ft.setDescripcion(rs.getString("descripcion"));
                    ft.setFecha(rs.getDate("fecha").toString());
                    return ft;
                }
            }
        }
        return null;
    }

    /**
     * Para bloquear días ya ocupados por otras sesiones.
     */
    public static Set<LocalDate> obtenerFechasOcupadas(Connection conexion, int idPeriodoEscolar)
            throws SQLException {

        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "SELECT fecha FROM fechaTutoria WHERE idPeriodoEscolar = ?";
        Set<LocalDate> set = new HashSet<>();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPeriodoEscolar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    set.add(rs.getDate("fecha").toLocalDate());
                }
            }
        }
        return set;
    }


    public static void guardarOActualizar(Connection conexion, FechaTutoria ft) throws SQLException {

        if (conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        if (ft == null) throw new SQLException("FechaTutoria es null.");

        // 1) ¿Ya existe por periodo + sesión?
        Integer idExistente = null;
        String sqlExiste = "SELECT idFechaTutoria FROM fechaTutoria WHERE idPeriodoEscolar = ? AND numSesion = ? LIMIT 1";

        try (PreparedStatement ps = conexion.prepareStatement(sqlExiste)) {
            ps.setInt(1, ft.getIdPeriodoEscolar());
            ps.setInt(2, ft.getNumSesion());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) idExistente = rs.getInt("idFechaTutoria");
            }
        }

        if (idExistente == null) {
            String sqlInsert =
                "INSERT INTO fechaTutoria (idPeriodoEscolar, descripcion, numSesion, fecha) " +
                "VALUES (?, ?, ?, ?)";

            try (PreparedStatement ps = conexion.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, ft.getIdPeriodoEscolar());
                ps.setString(2, ft.getDescripcion());
                ps.setInt(3, ft.getNumSesion());
                ps.setDate(4, java.sql.Date.valueOf(LocalDate.parse(ft.getFecha())));

                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) ft.setIdFechaTutoria(keys.getInt(1));
                }
            }
        } else {
            String sqlUpdate =
                "UPDATE fechaTutoria SET descripcion = ?, fecha = ? " +
                "WHERE idFechaTutoria = ?";

            try (PreparedStatement ps = conexion.prepareStatement(sqlUpdate)) {
                ps.setString(1, ft.getDescripcion());
                ps.setDate(2, java.sql.Date.valueOf(LocalDate.parse(ft.getFecha())));
                ps.setInt(3, idExistente);

                ps.executeUpdate();
                ft.setIdFechaTutoria(idExistente);
            }
        }
    }
}
