package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.ProblematicaAcademicaDAO;
import gestortutoriasfx.modelo.pojo.ProblematicaAcademica;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class ProblematicaAcademicaImplementacion {
    public static HashMap<String, Object> obtenerProblematicasPorReporte(int idReporte) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        
        Connection conexion = ConexionBD.abrirConexionBD();
        
        if (conexion != null) {
            try {
                ArrayList<ProblematicaAcademica> lista = ProblematicaAcademicaDAO.obtenerProblematicasPorReporte(conexion, idReporte);
                
                respuesta.put("error", false);
                respuesta.put("problematicas", lista);
                
            } catch (SQLException e) {
                respuesta.put("mensaje", "Error al cargar las problemáticas: " + e.getMessage());
                e.printStackTrace();
            } finally {
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "No hay conexión con la base de datos.");
        }
        
        return respuesta;
    }
}