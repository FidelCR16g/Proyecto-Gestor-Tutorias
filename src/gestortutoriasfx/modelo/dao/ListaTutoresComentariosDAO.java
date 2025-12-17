package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.TutorComentarioGeneral;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListaTutoresComentariosDAO {

    public static int eliminarPorReporte(Connection c, int idReporteGeneral) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");
        String sql = "DELETE FROM listaTutoresComentarios WHERE idReporteGeneral=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idReporteGeneral);
            return ps.executeUpdate();
        }
    }

    public static int insertarFila(Connection c, TutorComentarioGeneral fila, int idReporteGeneral) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "INSERT INTO listaTutoresComentarios (idReporteGeneral, idTutor, nombreTutor, comentario) " +
                     "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idReporteGeneral);

            if (fila.getIdTutor() == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, fila.getIdTutor());

            ps.setString(3, fila.getNombreTutor());
            ps.setString(4, fila.getComentario());
            return ps.executeUpdate();
        }
    }

    public static int insertarLote(Connection c, List<TutorComentarioGeneral> filas, int idReporteGeneral) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");
        if (filas == null || filas.isEmpty()) return 0;

        int insertadas = 0;
        for (TutorComentarioGeneral f : filas) {
            if (f == null) continue;
            insertadas += insertarFila(c, f, idReporteGeneral);
        }
        return insertadas;
    }

    public static ArrayList<TutorComentarioGeneral> obtenerPorReporte(Connection c, int idReporteGeneral) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "SELECT idListaTutoresComentarios, idReporteGeneral, idTutor, nombreTutor, comentario " +
                     "FROM listaTutoresComentarios WHERE idReporteGeneral=? ORDER BY nombreTutor ASC";

        ArrayList<TutorComentarioGeneral> lista = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idReporteGeneral);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TutorComentarioGeneral t = new TutorComentarioGeneral();
                    t.setIdListaTutoresComentarios(rs.getInt("idListaTutoresComentarios"));
                    t.setIdReporteGeneral(rs.getInt("idReporteGeneral"));
                    t.setIdTutor((Integer) rs.getObject("idTutor"));
                    t.setNombreTutor(rs.getString("nombreTutor"));
                    t.setComentario(rs.getString("comentario"));
                    lista.add(t);
                }
            }
        }
        return lista;
    }
}
