package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.FechaTutoria;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FechaTutoriaDAO {

    public static FechaTutoria obtenerPorPeriodoYNumSesion(Connection c, int idPeriodoEscolar, int numSesion) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "SELECT idFechaTutoria, idPeriodoEscolar, descripcion, numSesion, fechaInicio, fechaCierre " +
                     "FROM fechaTutoria WHERE idPeriodoEscolar=? AND numSesion=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idPeriodoEscolar);
            ps.setInt(2, numSesion);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public static ArrayList<FechaTutoria> obtenerPorPeriodo(Connection c, int idPeriodoEscolar) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "SELECT idFechaTutoria, idPeriodoEscolar, descripcion, numSesion, fechaInicio, fechaCierre " +
                     "FROM fechaTutoria WHERE idPeriodoEscolar=? ORDER BY numSesion ASC";

        ArrayList<FechaTutoria> lista = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idPeriodoEscolar);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public static int insertar(Connection c, FechaTutoria ft) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");
        if (ft == null) throw new SQLException("No hay información de FechaTutoria.");

        String sql = "INSERT INTO fechaTutoria (idPeriodoEscolar, descripcion, numSesion, fechaInicio, fechaCierre) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ft.getIdPeriodoEscolar());
            ps.setString(2, ft.getDescripcion());
            ps.setInt(3, ft.getNumSesion());

            Date fechaIni = Date.valueOf(ft.getFechaInicio());
            ps.setDate(4, fechaIni);

            if (ft.getFechaCierre() == null || ft.getFechaCierre().trim().isEmpty()) {
                ps.setNull(5, Types.DATE);
            } else {
                ps.setDate(5, Date.valueOf(ft.getFechaCierre().trim()));
            }

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public static int actualizar(Connection c, FechaTutoria ft) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");
        if (ft == null) throw new SQLException("No hay información de FechaTutoria.");

        String sql = "UPDATE fechaTutoria SET descripcion=?, fechaInicio=?, fechaCierre=? WHERE idFechaTutoria=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ft.getDescripcion());
            ps.setDate(2, Date.valueOf(ft.getFechaInicio()));

            if (ft.getFechaCierre() == null || ft.getFechaCierre().trim().isEmpty()) {
                ps.setNull(3, Types.DATE);
            } else {
                ps.setDate(3, Date.valueOf(ft.getFechaCierre().trim()));
            }

            ps.setInt(4, ft.getIdFechaTutoria());
            return ps.executeUpdate();
        }
    }

    public static int eliminarPorPeriodo(Connection c, int idPeriodoEscolar) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "DELETE FROM fechaTutoria WHERE idPeriodoEscolar=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idPeriodoEscolar);
            return ps.executeUpdate();
        }
    }

    private static FechaTutoria mapear(ResultSet rs) throws SQLException {
        FechaTutoria ft = new FechaTutoria();
        ft.setIdFechaTutoria(rs.getInt("idFechaTutoria"));
        ft.setIdPeriodoEscolar(rs.getInt("idPeriodoEscolar"));
        ft.setDescripcion(rs.getString("descripcion"));
        ft.setNumSesion(rs.getInt("numSesion"));

        Date inicio = rs.getDate("fechaInicio");
        Date cierre = (Date) rs.getObject("fechaCierre");

        if (inicio != null) {
            ft.setFecha(inicio.toString());
            ft.setFechaInicio();
        }
        if (cierre != null) {
            ft.setFecha(cierre.toString());
            ft.setFechaCierre();
        }

        // para que toString() y getFecha() representen siempre el inicio
        if (inicio != null) {
            ft.setFecha(inicio.toString());
        }

        return ft;
    }

    
    public static Set<LocalDate> obtenerFechasOcupadas(Connection c, int idPeriodoEscolar) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "SELECT fechaInicio, fechaCierre FROM fechaTutoria WHERE idPeriodoEscolar=?";

        Set<LocalDate> ocupadas = new HashSet<>();

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idPeriodoEscolar);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date iniSql = rs.getDate("fechaInicio");   // NOT NULL en BD
                    java.sql.Date cieSql = (java.sql.Date) rs.getObject("fechaCierre"); // puede ser NULL

                    if (iniSql == null) continue;

                    LocalDate ini = iniSql.toLocalDate();

                    if (cieSql == null) {
                        ocupadas.add(ini);
                    } else {
                        LocalDate cie = cieSql.toLocalDate();
                        if (cie.isBefore(ini)) {
                            ocupadas.add(ini);
                        } else {
                            LocalDate tmp = ini;
                            while (!tmp.isAfter(cie)) {
                                ocupadas.add(tmp);
                                tmp = tmp.plusDays(1);
                            }
                        }
                    }
                }
            }
        }

        return ocupadas;
    }
}
