package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CalendarioDAO {
    public static Map<LocalDate, Set<String>> obtenerCategoriasPorPeriodo(Connection conexion, int idPeriodoEscolar) throws SQLException {
        if (conexion == null) {
            throw new SQLException("No hay conexión con la base de datos.");
        }

        String sql = "SELECT dc.fecha, cd.categoria " +
                     "FROM diaCalendario dc " +
                     "LEFT JOIN categoriaDia cd ON cd.idDiaCalendario = dc.idDiaCalendario " +
                     "WHERE dc.idPeriodoEscolar = ?";

        Map<LocalDate, Set<String>> mapa = new HashMap<>();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPeriodoEscolar);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date fechaSql = rs.getDate("fecha");
                    if (fechaSql == null) continue;

                    LocalDate fecha = fechaSql.toLocalDate();
                    String categoria = rs.getString("categoria");

                    mapa.computeIfAbsent(fecha, k -> new HashSet<>());

                    if (categoria != null && !categoria.trim().isEmpty()) {
                        mapa.get(fecha).add(categoria.trim());
                    }
                }
            }
        }
        return mapa;
    }

    public static Set<LocalDate> obtenerFechasDelPeriodo(Connection conexion, int idPeriodoEscolar) throws SQLException {
        if (conexion == null) {
            throw new SQLException("No hay conexión con la base de datos.");
        }

        String sql = "SELECT fecha FROM diaCalendario WHERE idPeriodoEscolar = ?";
        Set<LocalDate> fechas = new HashSet<>();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idPeriodoEscolar);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date fechaSql = rs.getDate("fecha");
                    if (fechaSql != null) {
                        fechas.add(fechaSql.toLocalDate());
                    }
                }
            }
        }
        return fechas;
    }

    public static PeriodoEscolar obtenerPeriodoActual(Connection conexion) throws SQLException {
        if (conexion == null) {
            throw new SQLException("No hay conexión con la base de datos.");
        }

        String sql = "SELECT idPeriodoEscolar, rangoPeriodo, fechaInicio, fechaFin, estado, nombrePeriodoEscolar " +
                     "FROM periodoEscolar WHERE estado = true LIMIT 1";

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String fechaInicioStr = rs.getDate("fechaInicio").toString();
                String fechaFinStr = rs.getDate("fechaFin").toString();

                return new PeriodoEscolar(
                    rs.getInt("idPeriodoEscolar"),
                    rs.getString("rangoPeriodo"),
                    fechaInicioStr,
                    fechaFinStr,
                    rs.getBoolean("estado"),
                    rs.getString("nombrePeriodoEscolar")
                );
            }
        }
        return null;
    }
}
