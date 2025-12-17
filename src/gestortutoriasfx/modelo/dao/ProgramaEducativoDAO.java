package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.ProgramaEducativo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProgramaEducativoDAO {

    private static String normalizarEnum(String v) {
        if (v == null) return null;
        return v.trim()
                .replace("-", "_")
                .replace(" ", "_");
    }

    public static ArrayList<ProgramaEducativo> obtenerProgramas(Connection con) throws SQLException {
        if (con == null) throw new SQLException("No hay conexión con la base de datos.");

        ArrayList<ProgramaEducativo> lista = new ArrayList<>();

        String q =
            "SELECT idProgramaEducativo, idFacultad, idSupervisor, idCoordinador, nombre, plan, " +
            "area, nivel, modalidad, creditos, cupo, numPeriodos " +
            "FROM programaEducativo " +
            "ORDER BY nombre";

        try (PreparedStatement ps = con.prepareStatement(q);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProgramaEducativo pe = new ProgramaEducativo();
                pe.setIdProgramaEducativo(rs.getInt("idProgramaEducativo"));
                pe.setIdFacultad(rs.getInt("idFacultad"));

                int sup = rs.getInt("idSupervisor");
                pe.setIdSupervisor(rs.wasNull() ? null : sup);

                int coord = rs.getInt("idCoordinador");
                pe.setIdCoordinador(rs.wasNull() ? null : coord);

                pe.setNombre(rs.getString("nombre"));
                pe.setPlan(rs.getInt("plan"));

                // ===== area =====
                String area = normalizarEnum(rs.getString("area"));
                if (area != null && !area.isEmpty()) {
                    try {
                        pe.setArea(ProgramaEducativo.Area.valueOf(area));
                    } catch (IllegalArgumentException ex) {
                        System.err.println("Area inválida en BD: '" + rs.getString("area") + "'");
                        pe.setArea(null); // o pon un valor por defecto si tu enum lo tiene
                    }
                }

                String nivel = normalizarEnum(rs.getString("nivel"));
                if (nivel != null && !nivel.isEmpty()) {
                    try {
                        pe.setNivel(ProgramaEducativo.Nivel.valueOf(nivel));
                    } catch (IllegalArgumentException ex) {
                        System.err.println("Nivel inválido en BD: '" + rs.getString("nivel") + "'");
                        pe.setNivel(null);
                    }
                }

                // ===== modalidad =====
                String modalidad = normalizarEnum(rs.getString("modalidad"));
                if (modalidad != null && !modalidad.isEmpty()) {
                    try {
                        pe.setModalidad(ProgramaEducativo.Modalidad.valueOf(modalidad));
                    } catch (IllegalArgumentException ex) {
                        System.err.println("Modalidad inválida en BD: '" + rs.getString("modalidad") + "'");
                        pe.setModalidad(null);
                    }
                }

                pe.setCreditos(rs.getInt("creditos"));
                pe.setCupo(rs.getInt("cupo"));
                pe.setNumPeriodos(rs.getInt("numPeriodos"));

                lista.add(pe);
            }
        }

        return lista;
    }
}
