/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.TutorDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *
 * @author fave6
 */
public class TutorImplementacion {
    
    public static HashMap<String, Object> obtenerIdTutor(int idUsuario){
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        Connection conexion = null;
        
        try{
            conexion = ConexionBD.abrirConexionBD();
            ResultSet resultado = TutorDAO.obtenerIdTutor(conexion, idUsuario);
            
            if(resultado.next()) {
                respuesta.put("error", false);
                respuesta.put("idTutor", resultado.getInt("id_tutor"));
            }else {
                respuesta.put("error", true);
                respuesta.put("mensaje", "No se encontró información del tutor asociado.");
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