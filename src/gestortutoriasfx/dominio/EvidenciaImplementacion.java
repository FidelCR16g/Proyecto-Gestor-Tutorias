package gestortutoriasfx.dominio;

import gestortutoriasfx.modelo.ConexionBD;
import gestortutoriasfx.modelo.dao.EvidenciaDAO;
import gestortutoriasfx.modelo.pojo.Evidencia;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Nombre de la Clase: EvidenciaImplementacion
 *
 * Proyecto: Sistema de Gestión de Tutorías (SGT)
 *
 * Institución: Universidad Veracruzana
 * Curso: Principios de Construcción de Software
 *
 * Autor: Fidel Cruz Reyes
 * Fecha de Creación: 28/11/2025
 *
 * Descripción:
 * Se encarga de administrar la informacion de los distintos metodos de recuperacion de informacion de la 
 * evidencia de la tutoria de la base de datos.
 */

public class EvidenciaImplementacion {
    public static HashMap<String, Object> guardarCambiosEvidencias(
            List<Evidencia> nuevos, 
            List<Evidencia> eliminados, 
            int idTutor, 
            int numSesion) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        
        Connection conexion = ConexionBD.abrirConexionBD();
        if (conexion != null) {
            try {
                conexion.setAutoCommit(false);
                if (eliminados != null && !eliminados.isEmpty()) {
                    for (Evidencia ev : eliminados) {
                        EvidenciaDAO.eliminarEvidencia(conexion, ev.getIdEvidencia());
                    }
                }
                if (nuevos != null && !nuevos.isEmpty()) {
                    int idSesionDestino = EvidenciaDAO.obtenerIdSesionRepresentativa(conexion, idTutor, numSesion);
                    if (idSesionDestino > 0) {
                        for (Evidencia evidencia : nuevos) {
                            if (evidencia.isEsNuevo()) {
                                evidencia.setIdSesion(idSesionDestino);
                                EvidenciaDAO.guardarEvidencia(conexion, evidencia);
                            }
                        }
                    } else {
                        throw new SQLException("No se encontró una sesión válida para asociar los archivos.");
                    }
                }
                
                conexion.commit();
                respuesta.put("error", false);
                respuesta.put("mensaje", "Evidencias actualizadas correctamente.");
            } catch (SQLException e) {
                respuesta.put("mensaje", "Error en la transacción: " + e.getMessage());
                e.printStackTrace();
                try { conexion.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            } finally {
                try { conexion.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
                ConexionBD.cerrarConexion(conexion);
            }
        } else {
            respuesta.put("mensaje", "No hay conexión con la base de datos.");
        }
        return respuesta;
    }
    
    public static HashMap<String, Object> obtenerEvidenciasPorSesion(int idTutor, int numSesion) {
        HashMap<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("error", true);
        
        Connection conexion = ConexionBD.abrirConexionBD();
        
        if (conexion != null) {
            try {
                int idSesion = EvidenciaDAO.obtenerIdSesionRepresentativa(conexion, idTutor, numSesion);
                ArrayList<Evidencia> evidencias = new ArrayList<>();
                
                if (idSesion > 0) {
                    evidencias = EvidenciaDAO.obtenerEvidenciasPorIdSesion(conexion, idSesion);
                }
                
                respuesta.put("error", false);
                respuesta.put("evidencias", evidencias);
                
            } catch (SQLException e) {
                respuesta.put("mensaje", "Error SQL: " + e.getMessage());
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