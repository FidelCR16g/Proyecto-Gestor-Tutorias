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
        "SELECT pe.*, f.nombreFacultad AS nombreFacultad, " +
        "CONCAT(p_coord.nombre, ' ', p_coord.apellidoPaterno) AS nombreCoordinador, " +
        "CONCAT(p_super.nombre, ' ', p_super.apellidoPaterno) AS nombreSupervisor " +
        "FROM programaEducativo pe " +
        "INNER JOIN facultad f ON pe.idFacultad = f.idFacultad " +
        "LEFT JOIN coordinador c ON pe.idCoordinador = c.idCoordinador " +
        "LEFT JOIN profesor p_coord ON c.idProfesor = p_coord.idProfesor " +
        "LEFT JOIN supervisor s ON pe.idSupervisor = s.idSupervisor " +
        "LEFT JOIN profesor p_super ON s.idProfesor = p_super.idProfesor " +
        "ORDER BY pe.nombre";

        try (PreparedStatement ps = con.prepareStatement(q);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProgramaEducativo pe = new ProgramaEducativo();
                
                pe.setIdProgramaEducativo(rs.getInt("idProgramaEducativo"));
                pe.setIdFacultad(rs.getInt("idFacultad"));
                
                int idSup = rs.getInt("idSupervisor");
                pe.setIdSupervisor(rs.wasNull() ? 0 : idSup);

                int idCoord = rs.getInt("idCoordinador");
                pe.setIdCoordinador(rs.wasNull() ? 0 : idCoord);

                pe.setNombre(rs.getString("nombre"));
                pe.setPlan(rs.getInt("plan"));
                pe.setCreditos(rs.getInt("creditos"));
                pe.setCupo(rs.getInt("cupo"));
                pe.setNumPeriodos(rs.getInt("numPeriodos"));

                pe.setArea(mapearEnum(rs.getString("area"), ProgramaEducativo.Area.class));
                pe.setNivel(mapearEnum(rs.getString("nivel"), ProgramaEducativo.Nivel.class));
                pe.setModalidad(mapearEnum(rs.getString("modalidad"), ProgramaEducativo.Modalidad.class));
                
                pe.setNombreFacultad(rs.getString("nombreFacultad"));
                pe.setNombreCoordinador(rs.getString("nombreCoordinador"));
                pe.setNombreSupervisor(rs.getString("nombreSupervisor"));

                lista.add(pe);
            }
        }
        return lista;
    }
    
    private static <T extends Enum<T>> T mapearEnum(String valorBd, Class<T> enumClass) {
        String normalizado = normalizarEnum(valorBd);
        if (normalizado != null && !normalizado.isEmpty()) {
            try {
                return Enum.valueOf(enumClass, normalizado);
            } catch (IllegalArgumentException ex) {
                System.err.println("Valor inválido para Enum " + enumClass.getSimpleName() + ": '" + valorBd + "'");
            }
        }
        return null;
    }
}