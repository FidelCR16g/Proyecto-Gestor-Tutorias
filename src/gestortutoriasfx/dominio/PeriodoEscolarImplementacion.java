package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.PeriodoEscolarDAO;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Nombre de la Clase: PeriodoEscolarImplementacion
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 26/11/2025
 *
 * Descripción:
 * Se encarga de administrar la informacion de los distintos metodos de recuperacion de informacion del 
 * periodo de la base de datos.
 */

public class PeriodoEscolarImplementacion {
    public static HashMap<String, Object> obtenerPeriodoActual() {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        Connection conexion = ConexionBD.abrirConexionBD();
        
        if (conexion != null) {
            try {
                PeriodoEscolar periodo = PeriodoEscolarDAO.obtenerPeriodoActual(conexion);
                
                if (periodo != null) {
                    respuesta.put("error", false);
                    respuesta.put("periodo", periodo);
                } else {
                    respuesta.put("mensaje", "No hay ningún periodo escolar activo configurado en el sistema.");
                }
                
            } catch (SQLException e) {
                respuesta.put("mensaje", "Error en BD al obtener periodo: " + e.getMessage());
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
