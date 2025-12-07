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
        if(conexion == null) throw new SQLException("No hay conexión con la base de datos.");
        
        ArrayList<Salon> salones = new ArrayList<>(); 
        String consulta = "SELECT s.*, e.nombreEdificio FROM salon s " + 
               "INNER JOIN edificio e ON s.idEdificio = e.idEdificio " +
               "ORDER BY s.nombreSalon ASC";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                Salon salon = new Salon();
                salon.setIdSalon(resultado.getInt("idSalon"));
                salon.setNombreSalon(resultado.getString("nombreSalon") 
                        + " (" + resultado.getString("nombreEdificio") + ")");
                salones.add(salon);
            }
        }
        return salones;
    }
}