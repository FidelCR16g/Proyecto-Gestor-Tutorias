package gestortutoriasfx.modelo.dao;

import gestortutoriasfx.modelo.pojo.Salon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author fave6
 */
public class SalonDAO {
    public static ArrayList<Salon> obtenerTodosSalones(Connection conexion) throws SQLException {
        ArrayList<Salon> salones = new ArrayList<>();
        if (conexion != null) {
            String sql = "SELECT idSalon, nombreSalon FROM salon ORDER BY nombreSalon ASC";
            try (PreparedStatement sentencia = conexion.prepareStatement(sql);
                 ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    Salon s = new Salon();
                    s.setIdSalon(resultado.getInt("idSalon"));
                    s.setNombreSalon(resultado.getString("nombreSalon"));
                    salones.add(s);
                }
            }
        }
        return salones;
    }
}