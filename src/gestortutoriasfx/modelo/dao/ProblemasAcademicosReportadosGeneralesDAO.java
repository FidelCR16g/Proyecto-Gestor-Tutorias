package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.ProblemaAcademicoReportadoGeneral;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProblemasAcademicosReportadosGeneralesDAO {

    public static int eliminarPorReporte(Connection c, int idReporteGeneral) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");
        String sql = "DELETE FROM problemasAcademicosReportadosGenerales WHERE idReporteGeneral=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idReporteGeneral);
            return ps.executeUpdate();
        }
    }

    public static int insertarFila(Connection c, ProblemaAcademicoReportadoGeneral fila, int idReporteGeneral) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "INSERT INTO problemasAcademicosReportadosGenerales " +
                     "(idReporteGeneral, idExperienciaEducativa, nombreExperienciaEducativa, nombreProfesor, problema, numEstudiantes) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idReporteGeneral);

            if (fila.getIdExperienciaEducativa() == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, fila.getIdExperienciaEducativa());

            ps.setString(3, fila.getNombreExperienciaEducativa());
            ps.setString(4, fila.getNombreProfesor());
            ps.setString(5, fila.getProblema());
            ps.setInt(6, fila.getNumEstudiantes());

            return ps.executeUpdate();
        }
    }

    public static int insertarLote(Connection c, List<ProblemaAcademicoReportadoGeneral> filas, int idReporteGeneral) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");
        if (filas == null || filas.isEmpty()) return 0;

        int insertadas = 0;
        for (ProblemaAcademicoReportadoGeneral f : filas) {
            if (f == null) continue;
            insertadas += insertarFila(c, f, idReporteGeneral);
        }
        return insertadas;
    }

    public static ArrayList<ProblemaAcademicoReportadoGeneral> obtenerPorReporte(Connection c, int idReporteGeneral) throws SQLException {
        if (c == null) throw new SQLException("No hay conexión con la base de datos.");

        String sql = "SELECT idProblemasAcademicosReportadosGenerales, idReporteGeneral, idExperienciaEducativa, " +
                     "       nombreExperienciaEducativa, nombreProfesor, problema, numEstudiantes " +
                     "FROM problemasAcademicosReportadosGenerales WHERE idReporteGeneral=?";

        ArrayList<ProblemaAcademicoReportadoGeneral> lista = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idReporteGeneral);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProblemaAcademicoReportadoGeneral p = new ProblemaAcademicoReportadoGeneral();
                    p.setIdProblemasAcademicosReportadosGenerales(rs.getInt("idProblemasAcademicosReportadosGenerales"));
                    p.setIdReporteGeneral(rs.getInt("idReporteGeneral"));
                    p.setIdExperienciaEducativa((Integer) rs.getObject("idExperienciaEducativa"));
                    p.setNombreExperienciaEducativa(rs.getString("nombreExperienciaEducativa"));
                    p.setNombreProfesor(rs.getString("nombreProfesor"));
                    p.setProblema(rs.getString("problema"));
                    p.setNumEstudiantes(rs.getInt("numEstudiantes"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}
