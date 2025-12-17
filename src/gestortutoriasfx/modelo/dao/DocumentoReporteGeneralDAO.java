package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.DocumentoReporteGeneral;
import java.sql.*;
import java.util.ArrayList;

public class DocumentoReporteGeneralDAO {

    public static int guardarDocumentoUpsert(Connection c, int idReporteGeneral, String nombreArchivo, String tipoArchivo, byte[] archivo) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql =
            "INSERT INTO documentoReporteGeneral (idReporteGeneral, nombreArchivo, tipoArchivo, archivo) " +
            "VALUES (?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE tipoArchivo=VALUES(tipoArchivo), archivo=VALUES(archivo), fechaGeneracion=CURRENT_TIMESTAMP";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idReporteGeneral);
            ps.setString(2, nombreArchivo);
            ps.setString(3, tipoArchivo);
            ps.setBytes(4, archivo);
            return ps.executeUpdate();
        }
    }

    public static ArrayList<DocumentoReporteGeneral> listarMetadatosPorReporte(Connection c, int idReporteGeneral) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "SELECT idDocumentoGeneral, idReporteGeneral, nombreArchivo, tipoArchivo, fechaGeneracion " +
                     "FROM documentoReporteGeneral WHERE idReporteGeneral=? ORDER BY fechaGeneracion DESC";

        ArrayList<DocumentoReporteGeneral> lista = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idReporteGeneral);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DocumentoReporteGeneral d = new DocumentoReporteGeneral();
                    d.setIdDocumentoGeneral(rs.getInt("idDocumentoGeneral"));
                    d.setIdReporteGeneral(rs.getInt("idReporteGeneral"));
                    d.setNombreArchivo(rs.getString("nombreArchivo"));
                    d.setTipoArchivo(rs.getString("tipoArchivo"));

                    Timestamp ts = rs.getTimestamp("fechaGeneracion");
                    if (ts != null) d.setFechaGeneracion(ts.toLocalDateTime());

                    lista.add(d);
                }
            }
        }
        return lista;
    }

    public static DocumentoReporteGeneral obtenerDocumentoCompleto(Connection c, int idDocumentoGeneral) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "SELECT idDocumentoGeneral, idReporteGeneral, nombreArchivo, tipoArchivo, archivo, fechaGeneracion " +
                     "FROM documentoReporteGeneral WHERE idDocumentoGeneral=?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idDocumentoGeneral);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                DocumentoReporteGeneral d = new DocumentoReporteGeneral();
                d.setIdDocumentoGeneral(rs.getInt("idDocumentoGeneral"));
                d.setIdReporteGeneral(rs.getInt("idReporteGeneral"));
                d.setNombreArchivo(rs.getString("nombreArchivo"));
                d.setTipoArchivo(rs.getString("tipoArchivo"));
                d.setArchivo(rs.getBytes("archivo"));

                Timestamp ts = rs.getTimestamp("fechaGeneracion");
                if (ts != null) d.setFechaGeneracion(ts.toLocalDateTime());

                return d;
            }
        }
    }
}
