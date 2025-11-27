package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.PeriodoEscolarDAO;
import gestortutoriasfx.modelo.pojo.PeriodoEscolar;
import java.sql.Connection;
import java.sql.ResultSet;
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
    public static HashMap<String, Object> obtenerPeriodoActual(){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;
        
        try{
            PeriodoEscolar periodo = null;
            conexion = ConexionBD.abrirConexionBD();
            ResultSet resultado = PeriodoEscolarDAO.obtenerPeriodoActual(conexion);
            
            if(resultado.next()) {
                periodo = new PeriodoEscolar();
                periodo.setIdPeriodo(resultado.getInt("idPeriodo"));
                periodo.setNombre(resultado.getString("nombre"));
                periodo.setFechaInicio(resultado.getString("fechaInicio"));
                periodo.setFechaFin(resultado.getString("fechaFin"));
                
                respuesta.put("error", false);
                respuesta.put("periodo", periodo);
            }else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se encontró información del periodo actual.");
            }
        }catch (SQLException e) {
            respuesta.put("error", true);
            respuesta.put("mensaje", "Error BD: " + e.getMessage());
        }finally {
            ConexionBD.cerrarConexion(conexion);
        }
        return respuesta;
    }
}
