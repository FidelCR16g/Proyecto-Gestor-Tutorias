package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.SalonDAO;
import gestortutoriasfx.modelo.pojo.Salon;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SalonImplementacion {
    public static HashMap<String, Object> obtenerTodosSalones() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = ConexionBD.abrirConexionBD();

        try {
            ArrayList<Salon> listaSalones = SalonDAO.obtenerTodosSalones(conexion);
            respuesta.put("error", false);
            respuesta.put("salones", listaSalones);

        } catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error al cargar las fechas de tutoría: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ConexionBD.cerrarConexion(conexion);
        }
        
        return respuesta;
    }
}
