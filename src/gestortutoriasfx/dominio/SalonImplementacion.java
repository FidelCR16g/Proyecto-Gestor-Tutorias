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
        respuesta.put("error", true);

        Connection conexion = ConexionBD.abrirConexionBD();

        if (conexion != null) {
            try {
                ArrayList<Salon> listaSalones = SalonDAO.obtenerTodosSalones(conexion);
                
                respuesta.put("error", false);
                respuesta.put("salones", listaSalones);

            } catch (SQLException e) {
                respuesta.put("mensaje", "Error BD al cargar salones: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "Sin conexión con la base de datos.");
        }
        
        return respuesta;
    }
}
