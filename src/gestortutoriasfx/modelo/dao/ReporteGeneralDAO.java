package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.ReporteGeneral;
import java.sql.*;
import java.util.ArrayList;

public class ReporteGeneralDAO {
public static int insertarReporte(Connection c, ReporteGeneral r) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "INSERT INTO reporteGeneral (idPeriodoEscolar, idProgramaEducativo, idCoordinador, " +
                     "nombreProgramaEducativo, nombreCoordinador, nombrePeriodoEscolar, " +
                     "numSesion, fecha, objetivos, totalAlumnosRegistrados, totalAlumnosAsistieron, estatus, estadoLugar) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getIdPeriodoEscolar());

            if (r.getIdProgramaEducativo() <= 0) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, r.getIdProgramaEducativo());

            if (r.getIdCoordinador() <= 0) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, r.getIdCoordinador());

            ps.setString(4, r.getNombreProgramaEducativo());
            ps.setString(5, r.getNombreCoordinador());
            ps.setString(6, r.getNombrePeriodoEscolar());

            ps.setInt(7, r.getNumSesion());
            
            ps.setDate(8, r.getFecha() != null ? Date.valueOf(r.getFecha()) : null);
            
            ps.setString(9, r.getObjetivos());
            ps.setInt(10, r.getTotalAlumnosRegistrados());
            ps.setInt(11, r.getTotalAlumnosAsistieron());

            String estatus = (r.getEstatus() == null) ? "Borrador" : r.getEstatus().name();
            ps.setString(12, estatus);

            ps.setString(13, r.getEstadoLugar());

            int filas = ps.executeUpdate();
            if (filas <= 0) return 0;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public static int actualizarReporte(Connection c, ReporteGeneral r) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");
        if (r.getIdReporteGeneral() <= 0) throw new SQLException("ID del reporte general inválido.");

        String sql = "UPDATE reporteGeneral SET idPeriodoEscolar=?, idProgramaEducativo=?, idCoordinador=?, " +
                     "nombreProgramaEducativo=?, nombreCoordinador=?, nombrePeriodoEscolar=?, " +
                     "numSesion=?, fecha=?, objetivos=?, totalAlumnosRegistrados=?, totalAlumnosAsistieron=?, estatus=?, estadoLugar=? " +
                     "WHERE idReporteGeneral=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, r.getIdPeriodoEscolar());

            if (r.getIdProgramaEducativo() <= 0) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, r.getIdProgramaEducativo());

            if (r.getIdCoordinador() <= 0) ps.setNull(3, Types.INTEGER);
            else ps.setInt(3, r.getIdCoordinador());

            ps.setString(4, r.getNombreProgramaEducativo());
            ps.setString(5, r.getNombreCoordinador());
            ps.setString(6, r.getNombrePeriodoEscolar());

            ps.setInt(7, r.getNumSesion());
            ps.setDate(8, r.getFecha() != null ? Date.valueOf(r.getFecha()) : null);
            ps.setString(9, r.getObjetivos());

            ps.setInt(10, r.getTotalAlumnosRegistrados());
            ps.setInt(11, r.getTotalAlumnosAsistieron());

            String estatus = (r.getEstatus() == null) ? "Borrador" : r.getEstatus().name();
            ps.setString(12, estatus);

            ps.setString(13, r.getEstadoLugar());
            ps.setInt(14, r.getIdReporteGeneral());

            return ps.executeUpdate();
        }
    }

    public static ReporteGeneral obtenerPorId(Connection c, int idReporteGeneral) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "SELECT * FROM reporteGeneral WHERE idReporteGeneral=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idReporteGeneral);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapReporteGeneral(rs);
            }
        }
    }

    public static ArrayList<ReporteGeneral> listarPorCoordinador(Connection c, int idCoordinador, String busqueda) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String q = (busqueda == null) ? "" : busqueda.trim();
        String like = "%" + q + "%";

        String sql = "SELECT * FROM reporteGeneral " +
                     "WHERE (? = 0 OR idCoordinador = ?) " +
                     "  AND ( ? = '' " +
                     "        OR nombreProgramaEducativo LIKE ? " +
                     "        OR nombrePeriodoEscolar LIKE ? " +
                     "        OR estatus LIKE ? " +
                     "        OR CAST(numSesion AS CHAR) LIKE ? " +
                     "        OR CAST(fecha AS CHAR) LIKE ? ) " +
                     "ORDER BY fechaGeneracion DESC, numSesion DESC";

        ArrayList<ReporteGeneral> lista = new ArrayList<>();

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idCoordinador);
            ps.setInt(2, idCoordinador);

            ps.setString(3, q);
            ps.setString(4, like);
            ps.setString(5, like);
            ps.setString(6, like);
            ps.setString(7, like);
            ps.setString(8, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapReporteGeneral(rs));
                }
            }
        }
        return lista;
    }

    public static int actualizarEstatus(Connection c, int idReporteGeneral, String nuevoEstatus) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "UPDATE reporteGeneral SET estatus=? WHERE idReporteGeneral=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nuevoEstatus);
            ps.setInt(2, idReporteGeneral);
            return ps.executeUpdate();
        }
    }

    private static ReporteGeneral mapReporteGeneral(ResultSet rs) throws SQLException {
        ReporteGeneral r = new ReporteGeneral();
        r.setIdReporteGeneral(rs.getInt("idReporteGeneral"));
        r.setIdPeriodoEscolar(rs.getInt("idPeriodoEscolar"));
        
        int idProg = rs.getInt("idProgramaEducativo");
        r.setIdProgramaEducativo(rs.wasNull() ? 0 : idProg);

        int idCoord = rs.getInt("idCoordinador");
        r.setIdCoordinador(rs.wasNull() ? 0 : idCoord);

        r.setNombreProgramaEducativo(rs.getString("nombreProgramaEducativo"));
        r.setNombreCoordinador(rs.getString("nombreCoordinador"));
        r.setNombrePeriodoEscolar(rs.getString("nombrePeriodoEscolar"));

        r.setNumSesion(rs.getInt("numSesion"));

        Date fecha = rs.getDate("fecha");
        if (fecha != null) r.setFecha(fecha.toLocalDate());

        r.setObjetivos(rs.getString("objetivos"));
        r.setTotalAlumnosRegistrados(rs.getInt("totalAlumnosRegistrados"));
        r.setTotalAlumnosAsistieron(rs.getInt("totalAlumnosAsistieron"));
        
        r.setPorcentajeAsistencia(rs.getBigDecimal("porcentajeAsistencia"));

        String estatus = rs.getString("estatus");
        if (estatus != null) {
            try { 
                r.setEstatus(ReporteGeneral.Estatus.valueOf(estatus)); 
            } catch (IllegalArgumentException ex) { 
                r.setEstatus(ReporteGeneral.Estatus.Borrador); 
            }
        }

        Timestamp ts = rs.getTimestamp("fechaGeneracion");
        if (ts != null) r.setFechaGeneracion(ts.toLocalDateTime());

        r.setEstadoLugar(rs.getString("estadoLugar"));
        return r;
    }
}
